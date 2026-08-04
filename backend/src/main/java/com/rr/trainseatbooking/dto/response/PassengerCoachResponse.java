package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PassengerCoachResponse {

    private Long coachId;
    private String coachNumber;
    private String coachType;
    private Integer seatCapacity;

    private List<PassengerSeatResponse> seats;
}