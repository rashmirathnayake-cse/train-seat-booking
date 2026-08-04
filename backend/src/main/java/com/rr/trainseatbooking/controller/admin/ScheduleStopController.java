package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.request.ScheduleStopUpdateRequest;
import com.rr.trainseatbooking.dto.response.ScheduleStopResponse;
import com.rr.trainseatbooking.service.ScheduleStopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Schedule Stop Management",
        description = "Admin APIs for viewing and editing train timetable stops"
)
public class ScheduleStopController {

    private final ScheduleStopService scheduleStopService;

    @GetMapping(
            "/api/admin/train-schedules/{scheduleId}/stops"
    )
    @Operation(
            summary = "Get all stops for a train schedule"
    )
    public ResponseEntity<List<ScheduleStopResponse>>
    getStopsBySchedule(
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                scheduleStopService
                        .getStopsBySchedule(scheduleId)
        );
    }

    @GetMapping(
            "/api/admin/schedule-stops/{id}"
    )
    @Operation(
            summary = "Get a schedule stop by ID"
    )
    public ResponseEntity<ScheduleStopResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleStopService.getById(id)
        );
    }

    @PutMapping(
            "/api/admin/schedule-stops/{id}"
    )
    @Operation(
            summary = "Update arrival and departure times for one stop"
    )
    public ResponseEntity<ScheduleStopResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody
            ScheduleStopUpdateRequest request) {

        return ResponseEntity.ok(
                scheduleStopService.update(id, request)
        );
    }

    @PutMapping(
            "/api/admin/train-schedules/{scheduleId}/stops"
    )
    @Operation(
            summary = "Update the complete timetable",
            description = "Updates arrival and departure times for multiple schedule stops in one request."
    )
    public ResponseEntity<List<ScheduleStopResponse>>
    updateAll(
            @PathVariable Long scheduleId,
            @Valid
            @RequestBody
            List<ScheduleStopUpdateRequest> requests) {

        return ResponseEntity.ok(
                scheduleStopService.updateAll(
                        scheduleId,
                        requests
                )
        );
    }
}