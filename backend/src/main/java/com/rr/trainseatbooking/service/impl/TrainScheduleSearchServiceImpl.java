package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.projection.TrainScheduleSearchRow;
import com.rr.trainseatbooking.dto.response.TrainScheduleSearchResponse;
import com.rr.trainseatbooking.enums.BookingStatus;
import com.rr.trainseatbooking.enums.CoachType;
import com.rr.trainseatbooking.enums.ScheduleStatus;
import com.rr.trainseatbooking.exception.BadRequestException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.BookingRepository;
import com.rr.trainseatbooking.repository.SeatRepository;
import com.rr.trainseatbooking.repository.StationRepository;
import com.rr.trainseatbooking.repository.TrainScheduleRepository;
import com.rr.trainseatbooking.service.TrainScheduleSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainScheduleSearchServiceImpl
        implements TrainScheduleSearchService {

    private static final BigDecimal DEFAULT_RATE_PER_KM =
            new BigDecimal("4.00");

    private final TrainScheduleRepository trainScheduleRepository;
    private final StationRepository stationRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<TrainScheduleSearchResponse> search(
            Long originStationId,
            Long destinationStationId,
            LocalDate travelDate,
            LocalTime departureAfter) {

        validateRequest(
                originStationId,
                destinationStationId,
                travelDate,
                departureAfter
        );

        List<TrainScheduleSearchRow> schedules =
                trainScheduleRepository.searchSchedules(
                        originStationId,
                        destinationStationId,
                        travelDate,
                        departureAfter,
                        ScheduleStatus.ACTIVE
                );

        return schedules.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateRequest(
            Long originStationId,
            Long destinationStationId,
            LocalDate travelDate,
            LocalTime departureAfter) {

        if (originStationId.equals(destinationStationId)) {
            throw new BadRequestException(
                    "Origin and destination stations must be different."
            );
        }

        if (!stationRepository.existsById(originStationId)) {
            throw new ResourceNotFoundException(
                    "Origin station not found with id: "
                            + originStationId
            );
        }

        if (!stationRepository.existsById(destinationStationId)) {
            throw new ResourceNotFoundException(
                    "Destination station not found with id: "
                            + destinationStationId
            );
        }

        if (travelDate == null) {
            throw new BadRequestException(
                    "Travel date is required."
            );
        }

        if (departureAfter == null) {
            throw new BadRequestException(
                    "Earliest departure time is required."
            );
        }
    }

    private TrainScheduleSearchResponse mapToResponse(
            TrainScheduleSearchRow row) {

        double journeyDistance =
                row.getDestinationDistance()
                        - row.getOriginDistance();

        if (journeyDistance <= 0) {
            throw new BadRequestException(
                    "Invalid route distance configuration for schedule "
                            + row.getScheduleId()
            );
        }

        long totalReservedSeats =
                seatRepository.countByCoachTrainIdAndCoachType(
                        row.getTrainId(),
                        CoachType.RESERVED
                );

        long occupiedSeatCount =
                bookingRepository.countOccupiedSeats(
                        row.getScheduleId(),
                        row.getOriginStopOrder(),
                        row.getDestinationStopOrder(),
                        BookingStatus.CONFIRMED
                );

        long availableSeatCount =
                Math.max(
                        0,
                        totalReservedSeats - occupiedSeatCount
                );

        BigDecimal estimatedFare =
                BigDecimal.valueOf(journeyDistance)
                        .multiply(DEFAULT_RATE_PER_KM)
                        .setScale(2, RoundingMode.HALF_UP);

        return TrainScheduleSearchResponse.builder()
                .scheduleId(row.getScheduleId())
                .trainId(row.getTrainId())
                .trainNumber(row.getTrainNumber())
                .trainName(row.getTrainName())
                .travelDate(row.getTravelDate())
                .originStopId(row.getOriginStopId())
                .destinationStopId(
                        row.getDestinationStopId()
                )
                .departureTime(row.getDepartureTime())
                .arrivalTime(row.getArrivalTime())
                .durationMinutes(calculateDuration(
                        row.getDepartureTime(),
                        row.getArrivalTime()
                ))
                .journeyDistance(journeyDistance)
                .estimatedFare(estimatedFare)
                .availableSeatCount(availableSeatCount)
                .build();
    }

    private long calculateDuration(
            LocalTime departureTime,
            LocalTime arrivalTime) {

        long minutes = Duration.between(
                departureTime,
                arrivalTime
        ).toMinutes();

        /*
         * Supports a journey that passes midnight.
         */
        if (minutes < 0) {
            minutes += 24 * 60;
        }

        return minutes;
    }
}