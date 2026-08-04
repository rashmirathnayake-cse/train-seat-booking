package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.TrainScheduleRequest;
import com.rr.trainseatbooking.dto.response.TrainScheduleResponse;

import java.util.List;

public interface TrainScheduleService {

    TrainScheduleResponse create(
            TrainScheduleRequest request
    );

    List<TrainScheduleResponse> getAll();

    TrainScheduleResponse getById(Long id);

    TrainScheduleResponse update(
            Long id,
            TrainScheduleRequest request
    );

    void delete(Long id);

}