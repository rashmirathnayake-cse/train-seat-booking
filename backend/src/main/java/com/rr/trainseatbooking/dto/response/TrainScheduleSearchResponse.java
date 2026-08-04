package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class TrainScheduleSearchResponse {

    private Long scheduleId;

    private Long trainId;

    private String trainNumber;

    private String trainName;

    private LocalDate travelDate;

    private Long originStopId;

    private Long destinationStopId;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Long durationMinutes;

    private Double journeyDistance;

    private BigDecimal estimatedFare;

    private Long availableSeatCount;
}