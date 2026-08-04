package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SeatMapCoachResponse {

    private Long coachId;

    private String coachNumber;

    private String coachType;

    private Integer seatCapacity;

    private long availableSeatCount;

    private List<SeatMapSeatResponse> seats;
}