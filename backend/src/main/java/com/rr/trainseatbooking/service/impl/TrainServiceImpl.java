package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.TrainRequest;
import com.rr.trainseatbooking.dto.response.TrainResponse;
import com.rr.trainseatbooking.entity.Route;
import com.rr.trainseatbooking.entity.Train;
import com.rr.trainseatbooking.enums.CoachType;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.RouteRepository;
import com.rr.trainseatbooking.repository.TrainRepository;
import com.rr.trainseatbooking.service.TrainService;
import jakarta.transaction.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;

    private final RouteRepository routeRepository;

    @Override
    public TrainResponse createTrain(TrainRequest request) {

        if (trainRepository.existsByTrainNumber(request.getTrainNumber())) {
            throw new RuntimeException("Train number already exists.");
        }

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found"
                        ));

        Train train = Train.builder()
                .trainNumber(request.getTrainNumber())
                .name(request.getName())
                .description(request.getDescription())
                .active(request.getActive())
                .route(route)
                .build();

        Train SavedTrain = trainRepository.save(train);
        return getTrainInfo(SavedTrain);
    }

    @Override
    @Transactional
    public List<TrainResponse> getAllTrains() {
        return trainRepository.findAllTrainSummaries();
    }

    @Override
    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Train not found with id: " + id));
    }

    @Override
    @Transactional
    public TrainResponse getTrainInfoById(Long id) {

        Train train = getTrainById(id);
        return getTrainInfo(train);

    }

    @Override
    public TrainResponse updateTrain(Long id, TrainRequest request) {

        Train train = getTrainById(id);

        train.setTrainNumber(request.getTrainNumber());
        train.setName(request.getName());
        train.setDescription(request.getDescription());
        train.setActive(request.getActive());

        return getTrainInfo(trainRepository.save(train));
    }

    @Override
    public void deleteTrain(Long id) {

        Train train = getTrainById(id);

        trainRepository.delete(train);
    }

    private TrainResponse getTrainInfo(Train train){
        long totalCoachCount = train.getCoaches() == null
                ? 0
                : train.getCoaches().size();

        long reservedCoachCount = train.getCoaches() == null
                ? 0
                : train.getCoaches()
                .stream()
                .filter(coach ->
                        coach.getType() == CoachType.RESERVED
                )
                .count();

        return TrainResponse.builder()
                .trainId(train.getId())
                .routeId(
                        train.getRoute() != null
                                ? train.getRoute().getId()
                                : null
                )
                .trainNumber(train.getTrainNumber())
                .trainName(train.getName())
                .description(train.getDescription())
                .totalCoachCount(totalCoachCount)
                .reservedCoachCount(reservedCoachCount)
                .build();
    }
}
