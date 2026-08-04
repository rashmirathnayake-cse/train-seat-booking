package com.rr.trainseatbooking.dto.projection;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class TrainScheduleSearchRow {

    private final Long scheduleId;
    private final Long trainId;
    private final String trainNumber;
    private final String trainName;
    private final LocalDate travelDate;

    private final Long originStopId;
    private final Long destinationStopId;

    private final LocalTime departureTime;
    private final LocalTime arrivalTime;

    private final Integer originStopOrder;
    private final Integer destinationStopOrder;

    private final Double originDistance;
    private final Double destinationDistance;

    public TrainScheduleSearchRow(
            Long scheduleId,
            Long trainId,
            String trainNumber,
            String trainName,
            LocalDate travelDate,
            Long originStopId,
            Long destinationStopId,
            LocalTime departureTime,
            LocalTime arrivalTime,
            Integer originStopOrder,
            Integer destinationStopOrder,
            Double originDistance,
            Double destinationDistance) {

        this.scheduleId = scheduleId;
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.travelDate = travelDate;
        this.originStopId = originStopId;
        this.destinationStopId = destinationStopId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.originStopOrder = originStopOrder;
        this.destinationStopOrder = destinationStopOrder;
        this.originDistance = originDistance;
        this.destinationDistance = destinationDistance;
    }
}