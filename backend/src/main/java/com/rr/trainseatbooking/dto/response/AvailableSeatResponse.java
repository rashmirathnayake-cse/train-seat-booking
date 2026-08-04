package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailableSeatResponse {

    private Long seatId;

    private String seatNumber;

    private Long coachId;

    private String coachNumber;

    private String coachType;
}