package com.rr.trainseatbooking.dto.request;

import com.rr.trainseatbooking.enums.ScheduleStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class TrainScheduleRequest {

    private Long trainId;

    private LocalDate travelDate;

    private ScheduleStatus status;

}