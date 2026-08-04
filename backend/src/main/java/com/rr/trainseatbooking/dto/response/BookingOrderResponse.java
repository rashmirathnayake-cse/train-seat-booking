package com.rr.trainseatbooking.dto.response;

import com.rr.trainseatbooking.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class BookingOrderResponse {

    private String orderReference;

    private String passengerName;

    private String phone;

    private Long trainScheduleId;

    private String trainNumber;

    private String trainName;

    private LocalDate travelDate;

    private String originStation;

    private String destinationStation;

    private BigDecimal totalFare;

    private BookingStatus status;

    private List<BookedSeatResponse> seats;
}