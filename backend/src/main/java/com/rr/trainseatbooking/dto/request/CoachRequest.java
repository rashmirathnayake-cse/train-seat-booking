package com.rr.trainseatbooking.dto.request;


import com.rr.trainseatbooking.enums.CoachType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CoachRequest {


    private String coachNumber;


    private CoachType type;


    private Integer seatCapacity;


    private Long trainId;

}