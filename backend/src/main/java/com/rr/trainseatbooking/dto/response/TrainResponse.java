package com.rr.trainseatbooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrainResponse {

    private Long trainId;
    private String trainNumber;
    private String trainName;
    private String description;
    private Long routeId;
    private long totalCoachCount;
    private long reservedCoachCount;
}