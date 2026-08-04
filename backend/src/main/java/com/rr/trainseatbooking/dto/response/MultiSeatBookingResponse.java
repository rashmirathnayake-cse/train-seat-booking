package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class MultiSeatBookingResponse {

    private String orderReference;

    private String passengerName;

    private String phone;

    private Long trainScheduleId;

    private String originStation;

    private String destinationStation;

    private BigDecimal totalFare;

    private List<BookingResponse> bookings;
}