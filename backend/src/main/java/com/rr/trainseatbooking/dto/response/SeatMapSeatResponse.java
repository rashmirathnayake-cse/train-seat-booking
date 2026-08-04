package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatMapSeatResponse {

    private Long seatId;

    private String seatNumber;

    private boolean available;

    // For a graphical layout.
//    private Integer rowNumber;
//
//    private Integer columnNumber;
//
//    private String position;
}