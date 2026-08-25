package com.relaxstay.booking_service.model;

import com.relaxstay.booking_service.enumeration.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long hotelId;
    private String hotelName;
    private String hotelPlace;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfRooms;
    private Integer totalAmount;
    private BookingStatus status;
}

