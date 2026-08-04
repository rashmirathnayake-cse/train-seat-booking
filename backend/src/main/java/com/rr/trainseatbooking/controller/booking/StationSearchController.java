package com.rr.trainseatbooking.controller.booking;

import com.rr.trainseatbooking.service.StationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@Tag(
        name = "Passenger Station Search",
        description = "Passenger APIs for Station Search"
)
public class StationSearchController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<?> getAll(){

        return ResponseEntity.ok(
                stationService.getAllStations()
        );

    }
}
