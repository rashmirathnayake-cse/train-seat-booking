package com.rr.trainseatbooking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteStationRequest {


    private Long stationId;


    private Integer stopOrder;


    private Double distanceFromOrigin;


    private Boolean scheduledStop;

}