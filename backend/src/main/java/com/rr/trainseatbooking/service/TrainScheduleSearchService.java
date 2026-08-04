package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.response.TrainScheduleSearchResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrainScheduleSearchService {

    List<TrainScheduleSearchResponse> search(
            Long originStationId,
            Long destinationStationId,
            LocalDate travelDate,
            LocalTime departureAfter
    );
}