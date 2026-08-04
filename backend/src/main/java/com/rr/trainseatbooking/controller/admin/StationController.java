package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.request.StationRequest;
import com.rr.trainseatbooking.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stations")
@RequiredArgsConstructor
public class StationController {


    private final StationService stationService;



    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody StationRequest request
    ){

        return ResponseEntity.ok(
                stationService.createStation(request)
        );

    }



    @GetMapping
    public ResponseEntity<?> getAll(){

        return ResponseEntity.ok(
                stationService.getAllStations()
        );

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ){

        stationService.deleteStation(id);

        return ResponseEntity.ok().build();

    }

}