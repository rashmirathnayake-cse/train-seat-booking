package com.rr.trainseatbooking.controller.booking;

import com.rr.trainseatbooking.dto.request.BookingRequest;
import com.rr.trainseatbooking.dto.response.AvailableSeatResponse;
import com.rr.trainseatbooking.dto.response.BookingOrderResponse;
import com.rr.trainseatbooking.dto.response.BookingResponse;
import com.rr.trainseatbooking.dto.response.MultiSeatBookingResponse;
import com.rr.trainseatbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(
        name = "Passenger Booking",
        description = "Passenger APIs for seat availability and bookings"
)
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/available-seats")
    @Operation(
            summary = "Find available reserved seats",
            description = """
                    Returns seats that are not occupied by a confirmed
                    booking overlapping the selected journey segment.
                    """
    )
    public ResponseEntity<List<AvailableSeatResponse>>
    getAvailableSeats(
            @RequestParam Long scheduleId,
            @RequestParam Long originStopId,
            @RequestParam Long destinationStopId) {

        return ResponseEntity.ok(
                bookingService.getAvailableSeats(
                        scheduleId,
                        originStopId,
                        destinationStopId
                )
        );
    }

    @PostMapping
    @Operation(
            summary = "Create a booking",
            description = """
                    Books a selected reserved seat after transactionally
                    checking that the segment is still available.
                    """
    )
    public ResponseEntity<MultiSeatBookingResponse> create(
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.create(request));
    }

    @GetMapping("/{bookingReference}")
    @Operation(summary = "Find booking by reference")
    public ResponseEntity<BookingOrderResponse> getByReference(
            @PathVariable String bookingReference) {

        return ResponseEntity.ok(
                bookingService.getOrderByReference(
                        bookingReference
                )
        );
    }

    @PatchMapping("/{bookingReference}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<BookingResponse> cancel(
            @PathVariable String bookingReference) {

        return ResponseEntity.ok(
                bookingService.cancel(
                        bookingReference
                )
        );
    }
}