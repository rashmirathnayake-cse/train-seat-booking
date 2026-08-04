package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.ScheduleStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleStopRepository
        extends JpaRepository<ScheduleStop, Long> {

    List<ScheduleStop> findBySchedule_IdOrderByStopOrder(
            Long scheduleId
    );

    Optional<ScheduleStop> findByIdAndSchedule_Id(
            Long id,
            Long scheduleId
    );


}