package com.rr.trainseatbooking.controller.booking;

import com.rr.trainseatbooking.dto.response.PassengerTrainResponse;
import com.rr.trainseatbooking.service.PassengerTrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class PassengerTrainController {

    private final PassengerTrainService passengerTrainService;

    @GetMapping("/{trainId}")
    public ResponseEntity<PassengerTrainResponse> getTrain(
            @PathVariable Long trainId) {

        return ResponseEntity.ok(
                passengerTrainService.getTrain(trainId)
        );
    }


}