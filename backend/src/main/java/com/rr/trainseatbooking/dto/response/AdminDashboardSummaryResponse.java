package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AdminDashboardSummaryResponse {

    private long totalOrders;

    private long confirmedSeatBookings;

    private long cancelledSeatBookings;

    private BigDecimal totalRevenue;

    private BigDecimal todayRevenue;

    private long activeSchedules;

    private double averageOccupancyRate;
}