package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.dto.projection.TrainScheduleSearchRow;
import com.rr.trainseatbooking.entity.TrainSchedule;
import com.rr.trainseatbooking.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrainScheduleRepository
        extends JpaRepository<TrainSchedule, Long> {

    @Query("""
            SELECT new com.rr.trainseatbooking.dto.projection.TrainScheduleSearchRow(
                schedule.id,
                train.id,
                train.trainNumber,
                train.name,
                schedule.travelDate,
                originStop.id,
                destinationStop.id,
                originStop.departureTime,
                destinationStop.arrivalTime,
                originStop.stopOrder,
                destinationStop.stopOrder,
                originStop.distanceFromOrigin,
                destinationStop.distanceFromOrigin
            )
            FROM TrainSchedule schedule
            JOIN schedule.train train
            JOIN schedule.stops originStop
            JOIN schedule.stops destinationStop
            WHERE schedule.travelDate = :travelDate
              AND schedule.status = :status
              AND originStop.station.id = :originStationId
              AND destinationStop.station.id = :destinationStationId
              AND originStop.scheduledStop = true
              AND destinationStop.scheduledStop = true
              AND originStop.stopOrder < destinationStop.stopOrder
              AND originStop.departureTime IS NOT NULL
              AND destinationStop.arrivalTime IS NOT NULL
              AND originStop.departureTime >= :departureAfter
            ORDER BY originStop.departureTime ASC
            """)
    List<TrainScheduleSearchRow> searchSchedules(
            @Param("originStationId") Long originStationId,
            @Param("destinationStationId") Long destinationStationId,
            @Param("travelDate") LocalDate travelDate,
            @Param("departureAfter") LocalTime departureAfter,
            @Param("status") ScheduleStatus status
    );

    boolean existsByTrainIdAndTravelDate(Long trainId, LocalDate travelDate);

    List<TrainSchedule> findByStatus(
            ScheduleStatus status
    );
}