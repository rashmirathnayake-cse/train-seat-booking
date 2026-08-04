package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.request.RouteRequest;
import com.rr.trainseatbooking.dto.response.RouteResponse;
import com.rr.trainseatbooking.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/routes")
@RequiredArgsConstructor
@Tag(name = "Route Management")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponse> create(
            @RequestBody RouteRequest request) {

        return ResponseEntity.ok(routeService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {

        return ResponseEntity.ok(routeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(routeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @RequestBody RouteRequest request) {

        return ResponseEntity.ok(
                routeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        routeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}