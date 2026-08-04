package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.response.AdminDashboardSummaryResponse;
import com.rr.trainseatbooking.entity.ScheduleStop;
import com.rr.trainseatbooking.entity.TrainSchedule;
import com.rr.trainseatbooking.enums.BookingStatus;
import com.rr.trainseatbooking.enums.CoachType;
import com.rr.trainseatbooking.enums.ScheduleStatus;
import com.rr.trainseatbooking.repository.BookingRepository;
import com.rr.trainseatbooking.repository.ScheduleStopRepository;
import com.rr.trainseatbooking.repository.SeatRepository;
import com.rr.trainseatbooking.repository.TrainScheduleRepository;
import com.rr.trainseatbooking.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsServiceImpl
        implements AdminAnalyticsService {

    private final BookingRepository bookingRepository;
    private final TrainScheduleRepository trainScheduleRepository;
    private final ScheduleStopRepository scheduleStopRepository;
    private final SeatRepository seatRepository;

    @Override
    public AdminDashboardSummaryResponse getDashboardSummary() {

        long totalOrders =
                bookingRepository.countDistinctOrders();

        long confirmedSeatBookings =
                bookingRepository.countByStatus(
                        BookingStatus.CONFIRMED
                );

        long cancelledSeatBookings =
                bookingRepository.countByStatus(
                        BookingStatus.CANCELLED
                );

        BigDecimal totalRevenue =
                defaultZero(
                        bookingRepository.sumFareByStatus(
                                BookingStatus.CONFIRMED
                        )
                );

        LocalDate today = LocalDate.now();

        LocalDateTime startOfToday =
                today.atStartOfDay();

        LocalDateTime startOfTomorrow =
                today.plusDays(1).atStartOfDay();

        BigDecimal todayRevenue =
                defaultZero(
                        bookingRepository
                                .sumFareByStatusAndCreatedAtBetween(
                                        BookingStatus.CONFIRMED,
                                        startOfToday,
                                        startOfTomorrow
                                )
                );

        List<TrainSchedule> activeSchedules =
                trainScheduleRepository.findByStatus(
                        ScheduleStatus.ACTIVE
                );

        double averageOccupancyRate =
                calculateOverallOccupancy(activeSchedules);

        return AdminDashboardSummaryResponse.builder()
                .totalOrders(totalOrders)
                .confirmedSeatBookings(
                        confirmedSeatBookings
                )
                .cancelledSeatBookings(
                        cancelledSeatBookings
                )
                .totalRevenue(totalRevenue)
                .todayRevenue(todayRevenue)
                .activeSchedules(activeSchedules.size())
                .averageOccupancyRate(
                        averageOccupancyRate
                )
                .build();
    }

    private double calculateOverallOccupancy(
            List<TrainSchedule> schedules) {

        long totalSeatLegCapacity = 0;
        long totalOccupiedSeatLegs = 0;

        for (TrainSchedule schedule : schedules) {

            long reservedSeatCount =
                    seatRepository
                            .countByCoachTrainIdAndCoachType(
                                    schedule.getTrain().getId(),
                                    CoachType.RESERVED
                            );

            if (reservedSeatCount == 0) {
                continue;
            }

            List<ScheduleStop> stops =
                    scheduleStopRepository
                            .findBySchedule_IdOrderByStopOrder(
                                    schedule.getId()
                            );

            if (stops.size() < 2) {
                continue;
            }

            /*
             * Each adjacent pair of schedule stops represents
             * one physical journey segment.
             */
            for (int index = 0;
                 index < stops.size() - 1;
                 index++) {

                ScheduleStop segmentStart =
                        stops.get(index);

                ScheduleStop segmentEnd =
                        stops.get(index + 1);

                totalSeatLegCapacity +=
                        reservedSeatCount;

                long occupiedSeats =
                        bookingRepository
                                .countOccupiedSeatsForSegment(
                                        schedule.getId(),
                                        segmentStart.getStopOrder(),
                                        segmentEnd.getStopOrder(),
                                        BookingStatus.CONFIRMED
                                );

                totalOccupiedSeatLegs +=
                        occupiedSeats;
            }
        }

        if (totalSeatLegCapacity == 0) {
            return 0.0;
        }

        BigDecimal occupancy =
                BigDecimal.valueOf(
                                totalOccupiedSeatLegs
                        )
                        .multiply(
                                BigDecimal.valueOf(100)
                        )
                        .divide(
                                BigDecimal.valueOf(
                                        totalSeatLegCapacity
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        return occupancy.doubleValue();
    }

    private BigDecimal defaultZero(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }
}