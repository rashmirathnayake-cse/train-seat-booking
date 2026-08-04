package com.rr.trainseatbooking.controller.admin;

import com.rr.trainseatbooking.dto.response.AdminDashboardSummaryResponse;
import com.rr.trainseatbooking.service.AdminAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
@Tag(
        name = "Admin Analytics",
        description = "Admin dashboard and reporting APIs"
)
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/summary")
    @Operation(
            summary = "Get admin dashboard summary",
            description = """
                    Returns booking counts, revenue,
                    active schedule count and overall
                    segment-based occupancy.
                    """
    )
    public ResponseEntity<AdminDashboardSummaryResponse>
    getDashboardSummary() {

        return ResponseEntity.ok(
                adminAnalyticsService
                        .getDashboardSummary()
        );
    }
}