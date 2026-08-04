package com.rr.trainseatbooking.controller.booking;

import com.rr.trainseatbooking.dto.response.TrainScheduleSearchResponse;
import com.rr.trainseatbooking.service.TrainScheduleSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/train-schedules")
@RequiredArgsConstructor
@Tag(
        name = "Train Schedule Search",
        description = "Passenger APIs for finding suitable train schedules"
)
public class TrainScheduleSearchController {

    private final TrainScheduleSearchService searchService;



    @GetMapping("/search")
    @Operation(
            summary = "Search suitable train schedules",
            description = """
                    Finds active train schedules that stop at both selected
                    stations, travel in the correct direction, and depart
                    the origin station at or after the requested time.
                    """
    )
    public ResponseEntity<List<TrainScheduleSearchResponse>> search(
            @RequestParam Long originStationId,
            @RequestParam Long destinationStationId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate travelDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime departureAfter) {

        return ResponseEntity.ok(
                searchService.search(
                        originStationId,
                        destinationStationId,
                        travelDate,
                        departureAfter
                )
        );
    }
}