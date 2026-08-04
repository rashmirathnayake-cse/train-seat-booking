package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.response.PassengerCoachResponse;
import com.rr.trainseatbooking.dto.response.PassengerTrainResponse;
import com.rr.trainseatbooking.dto.response.SeatMapCoachResponse;
import com.rr.trainseatbooking.dto.response.SeatMapResponse;
import com.rr.trainseatbooking.dto.response.SeatMapSeatResponse;
import com.rr.trainseatbooking.entity.Coach;
import com.rr.trainseatbooking.entity.ScheduleStop;
import com.rr.trainseatbooking.entity.Seat;
import com.rr.trainseatbooking.entity.Train;
import com.rr.trainseatbooking.entity.TrainSchedule;
import com.rr.trainseatbooking.enums.BookingStatus;
import com.rr.trainseatbooking.enums.CoachType;
import com.rr.trainseatbooking.exception.BadRequestException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.BookingRepository;
import com.rr.trainseatbooking.repository.CoachRepository;
import com.rr.trainseatbooking.repository.ScheduleStopRepository;
import com.rr.trainseatbooking.repository.SeatRepository;
import com.rr.trainseatbooking.repository.TrainRepository;
import com.rr.trainseatbooking.repository.TrainScheduleRepository;
import com.rr.trainseatbooking.service.PassengerTrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassengerTrainServiceImpl
        implements PassengerTrainService {

    private final TrainRepository trainRepository;
    private final TrainScheduleRepository trainScheduleRepository;
    private final ScheduleStopRepository scheduleStopRepository;
    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Override
    public PassengerTrainResponse getTrain(Long trainId) {

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Train not found with id: " + trainId
                        ));

        List<PassengerCoachResponse> coaches =
                coachRepository
                        .findByTrainIdOrderByCoachNumber(trainId)
                        .stream()
                        .map(this::mapPassengerCoach)
                        .toList();

        return PassengerTrainResponse.builder()
                .trainId(train.getId())
                .trainNumber(train.getTrainNumber())
                .trainName(train.getName())
                .description(train.getDescription())
                .routeId(
                        train.getRoute() != null
                                ? train.getRoute().getId()
                                : null
                )
                .routeName(
                        train.getRoute() != null
                                ? train.getRoute().getName()
                                : null
                )
                .coaches(coaches)
                .build();
    }

    @Override
    public SeatMapResponse getSeatMap(
            Long scheduleId,
            Long originStopId,
            Long destinationStopId) {

        TrainSchedule schedule =
                trainScheduleRepository.findById(scheduleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Train schedule not found with id: "
                                                + scheduleId
                                ));

        ScheduleStop originStop =
                findStopInSchedule(
                        originStopId,
                        scheduleId,
                        "Origin"
                );

        ScheduleStop destinationStop =
                findStopInSchedule(
                        destinationStopId,
                        scheduleId,
                        "Destination"
                );

        validateJourney(originStop, destinationStop);

        List<Long> occupiedSeatIds =
                bookingRepository.findOccupiedSeatIds(
                        scheduleId,
                        originStop.getStopOrder(),
                        destinationStop.getStopOrder(),
                        BookingStatus.CONFIRMED
                );

        Set<Long> occupiedSeatIdSet =
                new HashSet<>(occupiedSeatIds);

        List<Coach> reservedCoaches =
                coachRepository
                        .findByTrainIdAndTypeOrderByCoachNumber(
                                schedule.getTrain().getId(),
                                CoachType.RESERVED
                        );

        List<SeatMapCoachResponse> coachResponses =
                reservedCoaches.stream()
                        .map(coach ->
                                mapSeatMapCoach(
                                        coach,
                                        occupiedSeatIdSet
                                )
                        )
                        .toList();

        return SeatMapResponse.builder()
                .scheduleId(schedule.getId())
                .trainId(schedule.getTrain().getId())
                .trainNumber(
                        schedule.getTrain().getTrainNumber()
                )
                .trainName(
                        schedule.getTrain().getName()
                )
                .travelDate(schedule.getTravelDate())
                .originStopId(originStop.getId())
                .originStation(
                        originStop.getStation().getName()
                )
                .destinationStopId(destinationStop.getId())
                .destinationStation(
                        destinationStop.getStation().getName()
                )
                .coaches(coachResponses)
                .build();
    }

    private ScheduleStop findStopInSchedule(
            Long stopId,
            Long scheduleId,
            String stopType) {

        return scheduleStopRepository
                .findByIdAndSchedule_Id(
                        stopId,
                        scheduleId
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                stopType
                                        + " stop does not belong to schedule "
                                        + scheduleId
                        ));
    }

    private void validateJourney(
            ScheduleStop originStop,
            ScheduleStop destinationStop) {

        if (originStop.getId()
                .equals(destinationStop.getId())) {

            throw new BadRequestException(
                    "Origin and destination stops must be different."
            );
        }

        if (originStop.getStopOrder()
                >= destinationStop.getStopOrder()) {

            throw new BadRequestException(
                    "Destination stop must occur after the origin stop."
            );
        }

        if (!Boolean.TRUE.equals(
                originStop.getScheduledStop())) {

            throw new BadRequestException(
                    "The selected train does not stop at the origin station."
            );
        }

        if (!Boolean.TRUE.equals(
                destinationStop.getScheduledStop())) {

            throw new BadRequestException(
                    "The selected train does not stop at the destination station."
            );
        }

        if (originStop.getDepartureTime() == null) {
            throw new BadRequestException(
                    "Departure time has not been configured for the origin."
            );
        }

        if (destinationStop.getArrivalTime() == null) {
            throw new BadRequestException(
                    "Arrival time has not been configured for the destination."
            );
        }
    }

    private PassengerCoachResponse mapPassengerCoach(
            Coach coach) {

        return PassengerCoachResponse.builder()
                .coachId(coach.getId())
                .coachNumber(coach.getCoachNumber())
                .coachType(coach.getType().name())
                .seatCapacity(coach.getSeatCapacity())
                .build();
    }

    private SeatMapCoachResponse mapSeatMapCoach(
            Coach coach,
            Set<Long> occupiedSeatIds) {

        List<SeatMapSeatResponse> seatResponses =
                seatRepository
                        .findByCoachIdOrderBySeatNumber(
                                coach.getId()
                        )
                        .stream()
                        .map(seat ->
                                mapSeat(
                                        seat,
                                        occupiedSeatIds
                                )
                        )
                        .toList();

        long availableSeatCount =
                seatResponses.stream()
                        .filter(SeatMapSeatResponse::isAvailable)
                        .count();

        return SeatMapCoachResponse.builder()
                .coachId(coach.getId())
                .coachNumber(coach.getCoachNumber())
                .coachType(coach.getType().name())
                .seatCapacity(coach.getSeatCapacity())
                .availableSeatCount(availableSeatCount)
                .seats(seatResponses)
                .build();
    }

    private SeatMapSeatResponse mapSeat(
            Seat seat,
            Set<Long> occupiedSeatIds) {

        boolean available =
                !occupiedSeatIds.contains(seat.getId());

        return SeatMapSeatResponse.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .available(available)
                .build();
    }
}