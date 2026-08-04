package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PassengerTrainResponse {

    private Long trainId;
    private String trainNumber;
    private String trainName;
    private Long routeId;
    private String routeName;
    private String description;

    private List<PassengerCoachResponse> coaches;
}