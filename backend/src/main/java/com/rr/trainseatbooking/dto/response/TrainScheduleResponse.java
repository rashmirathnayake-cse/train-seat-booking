package com.rr.trainseatbooking.dto.response;

import com.rr.trainseatbooking.enums.ScheduleStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class TrainScheduleResponse {

    private Long id;

    private Long trainId;

    private String trainNumber;

    private LocalDate travelDate;

    private ScheduleStatus status;

}