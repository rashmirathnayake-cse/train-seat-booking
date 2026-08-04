package com.rr.trainseatbooking.dto.request;

import lombok.*;

import java.time.LocalTime;


@Setter
@Getter
@NoArgsConstructor
public class ScheduleStopUpdateRequest {

    private Long id;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

}