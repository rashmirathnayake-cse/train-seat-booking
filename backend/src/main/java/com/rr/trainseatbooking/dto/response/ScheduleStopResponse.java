package com.rr.trainseatbooking.dto.response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Builder
public class ScheduleStopResponse {

    private Long id;

    private Long scheduleId;

    private Long stationId;

    private String stationName;

    private String stationCode;

    private Integer stopOrder;

    private Double distanceFromOrigin;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    private Boolean scheduledStop;

}