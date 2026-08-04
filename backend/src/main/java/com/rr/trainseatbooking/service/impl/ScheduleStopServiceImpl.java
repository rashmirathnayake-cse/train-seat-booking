package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.ScheduleStopUpdateRequest;
import com.rr.trainseatbooking.dto.response.ScheduleStopResponse;
import com.rr.trainseatbooking.entity.ScheduleStop;
import com.rr.trainseatbooking.exception.BadRequestException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.ScheduleStopRepository;
import com.rr.trainseatbooking.repository.TrainScheduleRepository;
import com.rr.trainseatbooking.service.ScheduleStopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleStopServiceImpl implements ScheduleStopService {

    private final ScheduleStopRepository scheduleStopRepository;
    private final TrainScheduleRepository trainScheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleStopResponse> getStopsBySchedule(Long scheduleId) {

        if (!trainScheduleRepository.existsById(scheduleId)) {
            throw new ResourceNotFoundException(
                    "Train schedule not found with id: " + scheduleId
            );
        }

        return scheduleStopRepository
                .findBySchedule_IdOrderByStopOrder(scheduleId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleStopResponse getById(Long id) {
        return mapToResponse(findScheduleStop(id));
    }

    @Override
    public ScheduleStopResponse update(
            Long id,
            ScheduleStopUpdateRequest request) {

        ScheduleStop stop = findScheduleStop(id);

        validateStopTimes(
                request.getArrivalTime(),
                request.getDepartureTime()
        );

        stop.setArrivalTime(request.getArrivalTime());
        stop.setDepartureTime(request.getDepartureTime());

        ScheduleStop updatedStop =
                scheduleStopRepository.save(stop);

        return mapToResponse(updatedStop);
    }

    @Override
    public List<ScheduleStopResponse> updateAll(
            Long scheduleId,
            List<ScheduleStopUpdateRequest> requests) {


        if (!trainScheduleRepository.existsById(scheduleId)) {
            throw new ResourceNotFoundException(
                    "Train schedule not found with id: " + scheduleId
            );
        }

        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException(
                    "At least one schedule stop is required."
            );
        }

        List<ScheduleStop> existingStops =
                scheduleStopRepository
                        .findBySchedule_IdOrderByStopOrder(scheduleId);

        Map<Long, ScheduleStop> stopMap =
                existingStops.stream()
                        .collect(Collectors.toMap(
                                ScheduleStop::getId,
                                Function.identity()
                        ));

        for (ScheduleStopUpdateRequest request : requests) {

            if (request.getId() == null) {


                throw new BadRequestException(
                        "Schedule stop id is required for bulk update."
                );
            }

            ScheduleStop stop = stopMap.get(request.getId());

            if (stop == null) {
                throw new BadRequestException(
                        "Schedule stop with id "
                                + request.getId()
                                + " does not belong to schedule "
                                + scheduleId
                );
            }

            validateStopTimes(
                    request.getArrivalTime(),
                    request.getDepartureTime()
            );

            stop.setArrivalTime(request.getArrivalTime());
            stop.setDepartureTime(request.getDepartureTime());
        }

        validateTimetableOrder(existingStops);

        List<ScheduleStop> savedStops =
                scheduleStopRepository.saveAll(existingStops);

        return savedStops.stream()
                .sorted((a, b) ->
                        a.getStopOrder()
                                .compareTo(b.getStopOrder()))
                .map(this::mapToResponse)
                .toList();
    }

    private ScheduleStop findScheduleStop(Long id) {

        return scheduleStopRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule stop not found with id: " + id
                        ));
    }

    private void validateStopTimes(
            LocalTime arrivalTime,
            LocalTime departureTime) {

        if (arrivalTime != null
                && departureTime != null
                && departureTime.isBefore(arrivalTime)) {

            throw new BadRequestException(
                    "Departure time cannot be before arrival time."
            );
        }
    }

    private void validateTimetableOrder(
            List<ScheduleStop> stops) {

        LocalTime previousTime = null;

        for (ScheduleStop stop : stops) {

            LocalTime arrival = stop.getArrivalTime();
            LocalTime departure = stop.getDepartureTime();

            if (arrival != null
                    && previousTime != null
                    && arrival.isBefore(previousTime)) {

                throw new BadRequestException(
                        "Arrival time at "
                                + stop.getStation().getName()
                                + " cannot be earlier than the previous stop."
                );
            }

            if (departure != null
                    && arrival != null
                    && departure.isBefore(arrival)) {

                throw new BadRequestException(
                        "Departure time at "
                                + stop.getStation().getName()
                                + " cannot be before arrival time."
                );
            }

            if (departure != null) {
                previousTime = departure;
            } else if (arrival != null) {
                previousTime = arrival;
            }
        }
    }

    private ScheduleStopResponse mapToResponse(
            ScheduleStop stop) {

        return ScheduleStopResponse.builder()
                .id(stop.getId())
                .scheduleId(stop.getSchedule().getId())
                .stationId(stop.getStation().getId())
                .stationName(stop.getStation().getName())
                .stationCode(stop.getStation().getCode())
                .stopOrder(stop.getStopOrder())
                .distanceFromOrigin(
                        stop.getDistanceFromOrigin()
                )
                .arrivalTime(stop.getArrivalTime())
                .departureTime(stop.getDepartureTime())
                .scheduledStop(stop.getScheduledStop())
                .build();
    }
}