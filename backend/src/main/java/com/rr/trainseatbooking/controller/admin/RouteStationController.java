package com.rr.trainseatbooking.controller.admin;


import com.rr.trainseatbooking.dto.request.RouteStationRequest;
import com.rr.trainseatbooking.dto.response.RouteStationResponse;
import com.rr.trainseatbooking.service.RouteStationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/routes")
@RequiredArgsConstructor
@Tag(name="Route Station Management")
public class RouteStationController {


    private final RouteStationService routeStationService;



    @PostMapping("/{routeId}/stations")
    public ResponseEntity<RouteStationResponse> addStation(
            @PathVariable Long routeId,
            @RequestBody RouteStationRequest request
    ){

        return ResponseEntity.ok(
                routeStationService.addStation(
                        routeId,
                        request
                )
        );
    }




    @GetMapping("/{routeId}/stations")
    public ResponseEntity<List<RouteStationResponse>> getStations(
            @PathVariable Long routeId
    ){

        return ResponseEntity.ok(
                routeStationService.getRouteStations(
                        routeId
                )
        );
    }




    @PutMapping("/stations/{id}")
    public ResponseEntity<RouteStationResponse> update(
            @PathVariable Long id,
            @RequestBody RouteStationRequest request
    ){

        return ResponseEntity.ok(
                routeStationService.update(
                        id,
                        request
                )
        );
    }




    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){

        routeStationService.delete(id);

        return ResponseEntity.noContent().build();

    }

}
