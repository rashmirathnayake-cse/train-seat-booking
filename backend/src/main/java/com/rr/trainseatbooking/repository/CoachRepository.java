package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.Coach;
import com.rr.trainseatbooking.enums.CoachType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    List<Coach> findByTrainId(Long trainId);

    List<Coach> findByTrainIdOrderByCoachNumber(Long trainId);

    List<Coach> findByTrainIdAndTypeOrderByCoachNumber(
            Long trainId,
            CoachType type
    );

}