package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.Booking;
import com.rr.trainseatbooking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Booking b
            WHERE b.trainSchedule.id = :scheduleId
              AND b.seat.id = :seatId
              AND b.status = :status
              AND b.originSequence < :destinationSequence
              AND b.destinationSequence > :originSequence
            """)
    boolean existsOverlappingBooking(
            @Param("scheduleId") Long scheduleId,
            @Param("seatId") Long seatId,
            @Param("originSequence") Integer originSequence,
            @Param("destinationSequence") Integer destinationSequence,
            @Param("status") BookingStatus status
    );

    @Query("""
            SELECT b.seat.id
            FROM Booking b
            WHERE b.trainSchedule.id = :scheduleId
              AND b.status = :status
              AND b.originSequence < :destinationSequence
              AND b.destinationSequence > :originSequence
            """)
    List<Long> findOccupiedSeatIds(
            @Param("scheduleId") Long scheduleId,
            @Param("originSequence") Integer originSequence,
            @Param("destinationSequence") Integer destinationSequence,
            @Param("status") BookingStatus status
    );

    Optional<Booking> findByBookingReference(
            String bookingReference
    );

    @Query("""
        SELECT COUNT(DISTINCT booking.seat.id)
        FROM Booking booking
        WHERE booking.trainSchedule.id = :scheduleId
          AND booking.status = :status
          AND booking.originSequence < :destinationSequence
          AND booking.destinationSequence > :originSequence
        """)
    long countOccupiedSeats(
            @Param("scheduleId") Long scheduleId,
            @Param("originSequence") Integer originSequence,
            @Param("destinationSequence") Integer destinationSequence,
            @Param("status") BookingStatus status
    );

    List<Booking> findByOrderReferenceOrderBySeatId(String orderReference);

    long countByStatus(BookingStatus status);

    @Query("""
            SELECT COUNT(DISTINCT booking.orderReference)
            FROM Booking booking
            WHERE booking.orderReference IS NOT NULL
            """)
    long countDistinctOrders();

    @Query("""
            SELECT COALESCE(SUM(booking.fare), 0)
            FROM Booking booking
            WHERE booking.status = :status
            """)
    BigDecimal sumFareByStatus(
            @Param("status") BookingStatus status
    );

    @Query("""
            SELECT COALESCE(SUM(booking.fare), 0)
            FROM Booking booking
            WHERE booking.status = :status
              AND booking.createdAt >= :start
              AND booking.createdAt < :end
            """)
    BigDecimal sumFareByStatusAndCreatedAtBetween(
            @Param("status") BookingStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT COUNT(DISTINCT booking.seat.id)
            FROM Booking booking
            WHERE booking.trainSchedule.id = :scheduleId
              AND booking.status = :status
              AND booking.originSequence < :segmentEnd
              AND booking.destinationSequence > :segmentStart
            """)
    long countOccupiedSeatsForSegment(
            @Param("scheduleId") Long scheduleId,
            @Param("segmentStart") Integer segmentStart,
            @Param("segmentEnd") Integer segmentEnd,
            @Param("status") BookingStatus status
    );
}