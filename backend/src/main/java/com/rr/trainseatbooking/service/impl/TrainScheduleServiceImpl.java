package com.rr.trainseatbooking.service.impl;


import com.rr.trainseatbooking.dto.request.TrainScheduleRequest;
import com.rr.trainseatbooking.dto.response.TrainScheduleResponse;
import com.rr.trainseatbooking.entity.RouteStation;
import com.rr.trainseatbooking.entity.ScheduleStop;
import com.rr.trainseatbooking.entity.Train;
import com.rr.trainseatbooking.entity.TrainSchedule;
import com.rr.trainseatbooking.exception.DuplicateResourceException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.RouteStationRepository;
import com.rr.trainseatbooking.repository.ScheduleStopRepository;
import com.rr.trainseatbooking.repository.TrainRepository;
import com.rr.trainseatbooking.repository.TrainScheduleRepository;
import com.rr.trainseatbooking.service.TrainScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainScheduleServiceImpl implements TrainScheduleService {

    private final TrainScheduleRepository trainScheduleRepository;
    private final TrainRepository trainRepository;
    private final RouteStationRepository routeStationRepository;
    private final ScheduleStopRepository scheduleStopRepository;

    @Override
    public TrainScheduleResponse create(TrainScheduleRequest request) {

        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Train not found with id : "
                                        + request.getTrainId()));

        if (trainScheduleRepository.existsByTrainIdAndTravelDate(
                request.getTrainId(),
                request.getTravelDate())) {

            throw new DuplicateResourceException(
                    "A schedule already exists for this train on "
                            + request.getTravelDate());
        }

        TrainSchedule schedule = TrainSchedule.builder()
                .train(train)
                .travelDate(request.getTravelDate())
                .status(request.getStatus())
                .build();

        TrainSchedule savedSchedule =
                trainScheduleRepository.save(schedule);

        List<RouteStation> routeStations =
                routeStationRepository
                        .findByRouteIdOrderByStopOrder(
                                train.getRoute().getId());

        if (routeStations.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Selected train's route has no stations.");
        }

        for (RouteStation rs : routeStations) {

            ScheduleStop stop = ScheduleStop.builder()
                    .schedule(savedSchedule)
                    .station(rs.getStation())
                    .stopOrder(rs.getStopOrder())
                    .distanceFromOrigin(rs.getDistanceFromOrigin())
                    .scheduledStop(rs.getScheduledStop())
                    .arrivalTime(null)
                    .departureTime(null)
                    .build();

            scheduleStopRepository.save(stop);
        }

        return mapToResponse(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainScheduleResponse> getAll() {

        return trainScheduleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainScheduleResponse getById(Long id) {

        TrainSchedule schedule =
                trainScheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Schedule not found with id : "
                                                + id));

        return mapToResponse(schedule);
    }

    @Override
    public TrainScheduleResponse update(
            Long id,
            TrainScheduleRequest request) {

        TrainSchedule schedule =
                trainScheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Schedule not found with id : "
                                                + id));

        if (!schedule.getTrain().getId().equals(request.getTrainId())) {

            Train train = trainRepository.findById(request.getTrainId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Train not found."));

            schedule.setTrain(train);
        }

        schedule.setTravelDate(request.getTravelDate());
        schedule.setStatus(request.getStatus());

        TrainSchedule updated =
                trainScheduleRepository.save(schedule);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        TrainSchedule schedule =
                trainScheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Schedule not found with id : "
                                                + id));

        trainScheduleRepository.delete(schedule);
    }

    private TrainScheduleResponse mapToResponse(
            TrainSchedule schedule) {

        return TrainScheduleResponse.builder()
                .id(schedule.getId())
                .trainId(schedule.getTrain().getId())
                .trainNumber(schedule.getTrain().getTrainNumber())
                .travelDate(schedule.getTravelDate())
                .status(schedule.getStatus())
                .build();
    }
}
