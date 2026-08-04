package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassengerSeatResponse {

    private Long seatId;
    private String seatNumber;

}