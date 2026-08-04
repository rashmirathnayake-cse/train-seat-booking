package com.rr.trainseatbooking.controller.booking;

import com.rr.trainseatbooking.dto.response.SeatMapResponse;
import com.rr.trainseatbooking.service.PassengerTrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/train-schedules")
@RequiredArgsConstructor
public class PassengerSeatMapController {

    private final PassengerTrainService passengerTrainService;

    @GetMapping("/{scheduleId}/seat-map")
    public ResponseEntity<SeatMapResponse> getSeatMap(
            @PathVariable Long scheduleId,
            @RequestParam Long originStopId,
            @RequestParam Long destinationStopId) {

        return ResponseEntity.ok(
                passengerTrainService.getSeatMap(
                        scheduleId,
                        originStopId,
                        destinationStopId
                )
        );
    }
}