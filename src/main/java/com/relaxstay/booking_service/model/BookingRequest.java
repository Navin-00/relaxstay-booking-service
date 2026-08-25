package com.relaxstay.booking_service.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BookingRequest {
        @NotNull(message = "UserId is required date is required")
        private Long userId;
        @NotNull(message = "hotelId is required")
        private Long hotelId;
        @NotNull(message = "Check-in date is required")
        private LocalDate checkInDate;
        @NotNull(message = "Check-out date is required")
        private LocalDate checkOutDate;
        @NotNull(message = "Number of rooms is required")
        @Positive(message = "Number of rooms must be greater than 0")
        private Integer numberOfRooms;
}
