package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.dto.response.TrainResponse;
import com.rr.trainseatbooking.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainRepository
        extends JpaRepository<Train, Long> {

    boolean existsByTrainNumber(String trainNumber);

    @Query("""
        SELECT new com.rr.trainseatbooking.dto.response.TrainResponse(
            train.id,
            train.trainNumber,
            train.name,
            train.description,
            train.route.id,
            COUNT(coach.id),
            SUM(
                CASE
                    WHEN coach.type = com.rr.trainseatbooking.enums.CoachType.RESERVED
                    THEN 1
                    ELSE 0
                END
            )
        )
        FROM Train train
        LEFT JOIN train.coaches coach
        GROUP BY
            train.id,
            train.trainNumber,
            train.name,
            train.description
        """)
    List<TrainResponse> findAllTrainSummaries();
}