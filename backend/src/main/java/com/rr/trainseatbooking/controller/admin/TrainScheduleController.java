package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.request.TrainScheduleRequest;
import com.rr.trainseatbooking.dto.response.TrainScheduleResponse;
import com.rr.trainseatbooking.service.TrainScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/train-schedules")
@RequiredArgsConstructor
@Tag(
        name = "Train Schedule Management",
        description = "Admin APIs for managing train schedules"
)
public class TrainScheduleController {

    private final TrainScheduleService trainScheduleService;

    @PostMapping
    @Operation(
            summary = "Create a train schedule",
            description = "Creates a train schedule and automatically generates schedule stops from the train route."
    )
    public ResponseEntity<TrainScheduleResponse> create(
            @Valid @RequestBody TrainScheduleRequest request) {

        TrainScheduleResponse response =
                trainScheduleService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all train schedules")
    public ResponseEntity<List<TrainScheduleResponse>> getAll() {

        return ResponseEntity.ok(
                trainScheduleService.getAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a train schedule by ID")
    public ResponseEntity<TrainScheduleResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trainScheduleService.getById(id)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a train schedule")
    public ResponseEntity<TrainScheduleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrainScheduleRequest request) {

        return ResponseEntity.ok(
                trainScheduleService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a train schedule",
            description = "Deletes the schedule and its generated schedule stops."
    )
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        trainScheduleService.delete(id);

        return ResponseEntity.noContent().build();
    }
}