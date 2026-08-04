package com.rr.trainseatbooking.dto.response;

import com.rr.trainseatbooking.enums.CoachType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoachResponse {

    private Long id;

    private String coachNumber;

    private CoachType type;

    private Integer seatCapacity;

    private Long trainId;

}