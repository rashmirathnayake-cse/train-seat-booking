package com.rr.trainseatbooking.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
public class BookedSeatResponse {

    private Long bookingId;

    private String bookingReference;

    private Long seatId;

    private String coachNumber;

    private String seatNumber;

    private BigDecimal fare;
}