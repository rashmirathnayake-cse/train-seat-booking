package com.rr.trainseatbooking.dto.response;

import com.rr.trainseatbooking.enums.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class BookingResponse {

    private Long id;

    private String bookingReference;

    private BookingStatus status;

    private String passengerName;

    private String phone;

    private Long trainScheduleId;

    private LocalDate travelDate;

    private String trainNumber;

    private Long seatId;

    private String seatNumber;

    private String coachNumber;

    private String originStation;

    private String destinationStation;

    private Integer originSequence;

    private Integer destinationSequence;

    private BigDecimal fare;
}