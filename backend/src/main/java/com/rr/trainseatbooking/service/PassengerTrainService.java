package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.response.PassengerTrainResponse;
import com.rr.trainseatbooking.dto.response.SeatMapResponse;

public interface PassengerTrainService {

    PassengerTrainResponse getTrain(Long trainId);

    SeatMapResponse getSeatMap(
            Long scheduleId,
            Long originStopId,
            Long destinationStopId
    );
}