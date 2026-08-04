package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class SeatMapResponse {

    private Long scheduleId;

    private Long trainId;

    private String trainNumber;

    private String trainName;

    private LocalDate travelDate;

    private Long originStopId;

    private String originStation;

    private Long destinationStopId;

    private String destinationStation;

    private List<SeatMapCoachResponse> coaches;
}