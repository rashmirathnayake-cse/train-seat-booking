package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.Seat;
import com.rr.trainseatbooking.enums.CoachType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s
            FROM Seat s
            JOIN FETCH s.coach c
            JOIN FETCH c.train t
            WHERE s.id = :seatId
            """)
    Optional<Seat> findByIdForUpdate(
            @Param("seatId") Long seatId
    );

    @Query("""
            SELECT s
            FROM Seat s
            JOIN FETCH s.coach c
            WHERE c.train.id = :trainId
              AND c.type = com.rr.trainseatbooking.enums.CoachType.RESERVED
              AND s.id NOT IN :occupiedSeatIds
            ORDER BY c.coachNumber, s.seatNumber
            """)
    List<Seat> findAvailableReservedSeats(
            @Param("trainId") Long trainId,
            @Param("occupiedSeatIds") List<Long> occupiedSeatIds
    );

    @Query("""
            SELECT s
            FROM Seat s
            JOIN FETCH s.coach c
            WHERE c.train.id = :trainId
              AND c.type = com.rr.trainseatbooking.enums.CoachType.RESERVED
            ORDER BY c.coachNumber, s.seatNumber
            """)
    List<Seat> findAllReservedSeatsByTrainId(
            @Param("trainId") Long trainId
    );

    long countByCoachTrainIdAndCoachType(
            Long trainId,
            CoachType coachType
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT seat
        FROM Seat seat
        JOIN FETCH seat.coach coach
        JOIN FETCH coach.train train
        WHERE seat.id IN :seatIds
        ORDER BY seat.id
        """)
    List<Seat> findAllByIdsForUpdate(
            @Param("seatIds") List<Long> seatIds
    );

    List<Seat> findByCoachIdOrderBySeatNumber(Long coachId);
}