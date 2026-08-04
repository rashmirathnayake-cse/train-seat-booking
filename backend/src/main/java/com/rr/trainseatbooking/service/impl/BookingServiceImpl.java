package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.BookingRequest;
import com.rr.trainseatbooking.dto.response.*;
import com.rr.trainseatbooking.entity.*;
import com.rr.trainseatbooking.enums.BookingStatus;
import com.rr.trainseatbooking.enums.CoachType;
import com.rr.trainseatbooking.exception.BadRequestException;
import com.rr.trainseatbooking.exception.BookingConflictException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.*;
import com.rr.trainseatbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final BigDecimal DEFAULT_RATE_PER_KM =
            new BigDecimal("4.00");

    private final BookingRepository bookingRepository;
    private final TrainScheduleRepository trainScheduleRepository;
    private final ScheduleStopRepository scheduleStopRepository;
    private final SeatRepository seatRepository;

    private Booking buildBooking(
            BookingRequest request,
            TrainSchedule schedule,
            StopPair stops,
            Seat seat,
            BigDecimal fare,
            String orderReference) {

        return Booking.builder()
                .bookingReference(
                        generateBookingReference()
                )
                .orderReference(orderReference)
                .passengerName(
                        request.getPassengerName().trim()
                )
                .phone(request.getPhone().trim())
                .originSequence(
                        stops.origin().getStopOrder()
                )
                .destinationSequence(
                        stops.destination().getStopOrder()
                )
                .fare(fare)
                .status(BookingStatus.CONFIRMED)
                .trainSchedule(schedule)
                .seat(seat)
                .originStop(stops.origin())
                .destinationStop(stops.destination())
                .build();
    }

    private String generateOrderReference() {

        return "ORD-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableSeatResponse> getAvailableSeats(
            Long scheduleId,
            Long originStopId,
            Long destinationStopId) {

        TrainSchedule schedule = findSchedule(scheduleId);

        StopPair stops = validateStops(
                scheduleId,
                originStopId,
                destinationStopId
        );

        List<Long> occupiedSeatIds =
                bookingRepository.findOccupiedSeatIds(
                        scheduleId,
                        stops.origin().getStopOrder(),
                        stops.destination().getStopOrder(),
                        BookingStatus.CONFIRMED
                );

        List<Seat> availableSeats;

        if (occupiedSeatIds.isEmpty()) {
            availableSeats =
                    seatRepository.findAllReservedSeatsByTrainId(
                            schedule.getTrain().getId()
                    );
        } else {
            availableSeats =
                    seatRepository.findAvailableReservedSeats(
                            schedule.getTrain().getId(),
                            occupiedSeatIds
                    );
        }

        return availableSeats.stream()
                .map(this::mapAvailableSeat)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingOrderResponse getOrderByReference(
            String orderReference) {

        List<Booking> bookings =
                bookingRepository.findByOrderReferenceOrderBySeatId(
                        orderReference
                );

        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Booking order not found with reference: "
                            + orderReference
            );
        }

        Booking first = bookings.getFirst();

        BigDecimal totalFare =
                bookings.stream()
                        .map(Booking::getFare)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        List<BookedSeatResponse> seats =
                bookings.stream()
                        .map(this::mapBookedSeat)
                        .toList();

        return BookingOrderResponse.builder()
                .orderReference(first.getOrderReference())
                .passengerName(first.getPassengerName())
                .phone(first.getPhone())
                .trainScheduleId(first.getTrainSchedule().getId())
                .trainNumber(first.getTrainSchedule()
                        .getTrain()
                        .getTrainNumber())
                .trainName(first.getTrainSchedule()
                        .getTrain()
                        .getName())
                .travelDate(first.getTrainSchedule()
                        .getTravelDate())
                .originStation(first.getOriginStop()
                        .getStation()
                        .getName())
                .destinationStation(first.getDestinationStop()
                        .getStation()
                        .getName())
                .status(first.getStatus())
                .totalFare(totalFare)
                .seats(seats)
                .build();
    }

    @Override
    @Transactional
    public MultiSeatBookingResponse create(
            BookingRequest request) {

        TrainSchedule schedule =
                findSchedule(request.getTrainScheduleId());

        StopPair stops = validateStops(
                schedule.getId(),
                request.getOriginStopId(),
                request.getDestinationStopId()
        );

        List<Long> distinctSeatIds =
                request.getSeatIds()
                        .stream()
                        .distinct()
                        .sorted()
                        .toList();

        if (distinctSeatIds.size()
                != request.getSeatIds().size()) {

            throw new BadRequestException(
                    "Duplicate seat IDs are not allowed."
            );
        }

        List<Seat> seats =
                seatRepository.findAllByIdsForUpdate(
                        distinctSeatIds
                );

        if (seats.size() != distinctSeatIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more selected seats were not found."
            );
        }

        /*
         * Validate every seat before creating anything.
         */
        for (Seat seat : seats) {

            validateSeatBelongsToScheduledTrain(
                    seat,
                    schedule
            );

            validateReservedCoach(seat);

            boolean conflict =
                    bookingRepository
                            .existsOverlappingBooking(
                                    schedule.getId(),
                                    seat.getId(),
                                    stops.origin()
                                            .getStopOrder(),
                                    stops.destination()
                                            .getStopOrder(),
                                    BookingStatus.CONFIRMED
                            );

            if (conflict) {
                throw new BookingConflictException(
                        "Seat "
                                + seat.getCoach()
                                .getCoachNumber()
                                + "-"
                                + seat.getSeatNumber()
                                + " is no longer available."
                );
            }
        }

        BigDecimal farePerSeat =
                calculateFare(
                        stops.origin(),
                        stops.destination()
                );

        String orderReference =
                generateOrderReference();

        List<Booking> bookings =
                seats.stream()
                        .map(seat ->
                                buildBooking(
                                        request,
                                        schedule,
                                        stops,
                                        seat,
                                        farePerSeat,
                                        orderReference
                                )
                        )
                        .toList();

        List<Booking> savedBookings =
                bookingRepository.saveAll(bookings);

        BigDecimal totalFare =
                farePerSeat.multiply(
                        BigDecimal.valueOf(
                                savedBookings.size()
                        )
                );

        return MultiSeatBookingResponse.builder()
                .orderReference(orderReference)
                .passengerName(
                        request.getPassengerName().trim()
                )
                .phone(request.getPhone().trim())
                .trainScheduleId(schedule.getId())
                .originStation(
                        stops.origin()
                                .getStation()
                                .getName()
                )
                .destinationStation(
                        stops.destination()
                                .getStation()
                                .getName()
                )
                .totalFare(totalFare)
                .bookings(
                        savedBookings.stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }


    @Override
    @Transactional
    public BookingResponse cancel(
            String bookingReference) {

        Booking booking = bookingRepository
                .findByBookingReference(bookingReference)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with reference: "
                                        + bookingReference
                        ));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException(
                    "Booking is already cancelled."
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return mapToResponse(
                bookingRepository.save(booking)
        );
    }

    private TrainSchedule findSchedule(Long scheduleId) {

        return trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Train schedule not found with id: "
                                        + scheduleId
                        ));
    }

    private StopPair validateStops(
            Long scheduleId,
            Long originStopId,
            Long destinationStopId) {

        if (originStopId.equals(destinationStopId)) {
            throw new BadRequestException(
                    "Origin and destination must be different."
            );
        }

        ScheduleStop origin =
                scheduleStopRepository
                        .findByIdAndSchedule_Id(
                                originStopId,
                                scheduleId
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Origin stop does not belong " +
                                                "to the selected schedule."
                                ));

        ScheduleStop destination =
                scheduleStopRepository
                        .findByIdAndSchedule_Id(
                                destinationStopId,
                                scheduleId
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Destination stop does not belong " +
                                                "to the selected schedule."
                                ));

        if (origin.getStopOrder()
                >= destination.getStopOrder()) {

            throw new BadRequestException(
                    "Destination must occur after the origin."
            );
        }

        if (!Boolean.TRUE.equals(origin.getScheduledStop())
                || !Boolean.TRUE.equals(
                destination.getScheduledStop())) {

            throw new BadRequestException(
                    "The selected train does not stop at " +
                            "one of the selected stations."
            );
        }

        if (origin.getDepartureTime() == null) {
            throw new BadRequestException(
                    "Origin departure time has not been configured."
            );
        }

        if (destination.getArrivalTime() == null) {
            throw new BadRequestException(
                    "Destination arrival time has not been configured."
            );
        }

        return new StopPair(origin, destination);
    }

    private void validateSeatBelongsToScheduledTrain(
            Seat seat,
            TrainSchedule schedule) {

        Long seatTrainId =
                seat.getCoach().getTrain().getId();

        Long scheduleTrainId =
                schedule.getTrain().getId();

        if (!seatTrainId.equals(scheduleTrainId)) {
            throw new BadRequestException(
                    "The selected seat does not belong " +
                            "to the scheduled train."
            );
        }
    }

    private void validateReservedCoach(Seat seat) {

        if (seat.getCoach().getType()
                != CoachType.RESERVED) {

            throw new BadRequestException(
                    "Only reserved coach seats can be booked."
            );
        }
    }

    private BigDecimal calculateFare(
            ScheduleStop origin,
            ScheduleStop destination) {

        BigDecimal distance =
                BigDecimal.valueOf(
                        destination.getDistanceFromOrigin()
                                - origin.getDistanceFromOrigin()
                );

        if (distance.signum() <= 0) {
            throw new BadRequestException(
                    "Invalid route distance configuration."
            );
        }

        return distance
                .multiply(DEFAULT_RATE_PER_KM)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String generateBookingReference() {

        return "BK-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private AvailableSeatResponse mapAvailableSeat(
            Seat seat) {

        return AvailableSeatResponse.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .coachId(seat.getCoach().getId())
                .coachNumber(
                        seat.getCoach().getCoachNumber()
                )
                .coachType(
                        seat.getCoach().getType().name()
                )
                .build();
    }

    private BookingResponse mapToResponse(
            Booking booking) {

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(
                        booking.getBookingReference()
                )
                .status(booking.getStatus())
                .passengerName(
                        booking.getPassengerName()
                )
                .phone(booking.getPhone())
                .trainScheduleId(
                        booking.getTrainSchedule().getId()
                )
                .travelDate(
                        booking.getTrainSchedule()
                                .getTravelDate()
                )
                .trainNumber(
                        booking.getTrainSchedule()
                                .getTrain()
                                .getTrainNumber()
                )
                .seatId(booking.getSeat().getId())
                .seatNumber(
                        booking.getSeat().getSeatNumber()
                )
                .coachNumber(
                        booking.getSeat()
                                .getCoach()
                                .getCoachNumber()
                )
                .originStation(
                        booking.getOriginStop()
                                .getStation()
                                .getName()
                )
                .destinationStation(
                        booking.getDestinationStop()
                                .getStation()
                                .getName()
                )
                .originSequence(
                        booking.getOriginSequence()
                )
                .destinationSequence(
                        booking.getDestinationSequence()
                )
                .fare(booking.getFare())
                .build();
    }

    private record StopPair(
            ScheduleStop origin,
            ScheduleStop destination) {
    }

    private BookedSeatResponse mapBookedSeat(
            Booking booking) {

        return BookedSeatResponse.builder()
                .bookingId(booking.getId())
                .bookingReference(
                        booking.getBookingReference()
                )
                .seatId(
                        booking.getSeat().getId()
                )
                .coachNumber(
                        booking.getSeat()
                                .getCoach()
                                .getCoachNumber()
                )
                .seatNumber(
                        booking.getSeat()
                                .getSeatNumber()
                )
                .fare(
                        booking.getFare()
                )
                .build();
    }
}