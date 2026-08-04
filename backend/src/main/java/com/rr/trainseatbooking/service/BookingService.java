package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.BookingRequest;
import com.rr.trainseatbooking.dto.response.AvailableSeatResponse;
import com.rr.trainseatbooking.dto.response.BookingOrderResponse;
import com.rr.trainseatbooking.dto.response.BookingResponse;
import com.rr.trainseatbooking.dto.response.MultiSeatBookingResponse;

import java.util.List;

public interface BookingService {

    List<AvailableSeatResponse> getAvailableSeats(
            Long scheduleId,
            Long originStopId,
            Long destinationStopId
    );

    MultiSeatBookingResponse create(BookingRequest request);

    BookingOrderResponse getOrderByReference(
            String orderReference
    );

    BookingResponse cancel(String bookingReference);
}