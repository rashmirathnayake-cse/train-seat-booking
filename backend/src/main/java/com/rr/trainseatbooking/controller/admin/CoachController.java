package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.request.CoachRequest;
import com.rr.trainseatbooking.dto.response.CoachResponse;
import com.rr.trainseatbooking.entity.Coach;
import com.rr.trainseatbooking.service.CoachService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/coaches")
@RequiredArgsConstructor
@Tag(
        name="Coach Management",
        description="Admin APIs for managing train coaches"
)
public class CoachController {


    private final CoachService coachService;



    @PostMapping
    @Operation(summary="Create coach and automatically generate seats")
    public ResponseEntity<CoachResponse> createCoach(
            @RequestBody CoachRequest request
    ){

        return ResponseEntity.ok(
                coachService.createCoach(request)
        );
    }



    @GetMapping
    public ResponseEntity<List<CoachResponse>> getAll(){

        return ResponseEntity.ok(
                coachService.getAllCoaches()
        );

    }



    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<CoachResponse>> getByTrain(
            @PathVariable Long trainId
    ){

        return ResponseEntity.ok(
                coachService.getCoachesByTrain(trainId)
        );

    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<Coach> getById(
            @PathVariable Long coachId
    ){

        return ResponseEntity.ok(
                coachService.getCoachById(coachId)
        );

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){

        coachService.deleteCoach(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update coach details")
    public ResponseEntity<CoachResponse> updateCoach(
            @PathVariable Long id,
            @RequestBody CoachRequest request
    ){

        return ResponseEntity.ok(
                coachService.updateCoach(id, request)
        );
    }

}