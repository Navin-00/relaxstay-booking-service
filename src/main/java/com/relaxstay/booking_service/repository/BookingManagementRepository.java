package com.relaxstay.booking_service.repository;

import com.relaxstay.booking_service.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingManagementRepository extends JpaRepository<Booking,Long> {
    @Query("""
    SELECT b FROM Booking b
    WHERE (:userId IS NULL OR b.userId = :userId)
      AND (:hotelId IS NULL OR b.hotelId = :hotelId)
      AND (:fromDate IS NULL OR b.checkInDate >= :fromDate)
      AND (:toDate IS NULL OR b.checkInDate <= :toDate)
""")
    List<Booking> searchBookings(
            @Param("userId") Long userId,
            @Param("hotelId") Long hotelId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
