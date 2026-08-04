package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.TrainRequest;
import com.rr.trainseatbooking.dto.response.TrainResponse;
import com.rr.trainseatbooking.entity.Train;

import java.util.List;

public interface TrainService {

    TrainResponse createTrain(TrainRequest request);

    List<TrainResponse> getAllTrains();

    Train getTrainById(Long id);

    TrainResponse getTrainInfoById(Long id);

    TrainResponse updateTrain(Long id, TrainRequest request);

    void deleteTrain(Long id);

}

