package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteStationResponse {


    private Long id;


    private Long stationId;


    private String stationName;


    private Integer stopOrder;


    private Double distanceFromOrigin;


    private Boolean scheduledStop;

}