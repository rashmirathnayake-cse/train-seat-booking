package com.rr.trainseatbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequest {

    @NotNull
    private Long trainScheduleId;

    @NotNull
    private List<Long> seatIds;

    @NotNull
    private Long originStopId;

    @NotNull
    private Long destinationStopId;

    @NotBlank
    private String passengerName;

    @NotBlank
    private String phone;
}