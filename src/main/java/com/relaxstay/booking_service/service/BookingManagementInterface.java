package com.relaxstay.booking_service.service;

import com.relaxstay.booking_service.model.BookingRequest;
import com.relaxstay.booking_service.model.BookingResponse;
import java.time.LocalDate;
import java.util.List;

public interface BookingManagementInterface {
    List<BookingResponse> getAllBookingDetails();
    BookingResponse bookHotel(BookingRequest bookingRequest);
    BookingResponse updateBooking(Long id,BookingRequest bookingRequest);
    BookingResponse cancelBooking(Long id);
    List<BookingResponse> searchBooking(Long userId, Long hotelId, LocalDate fromDate, LocalDate toDate);
}
