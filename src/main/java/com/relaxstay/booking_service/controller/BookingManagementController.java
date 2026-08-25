package com.relaxstay.booking_service.controller;

import com.relaxstay.booking_service.model.BookingRequest;
import com.relaxstay.booking_service.model.BookingResponse;
import com.relaxstay.booking_service.service.BookingManagementInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")

public class BookingManagementController {
    public final BookingManagementInterface bookingManagementInterface;

    @PostMapping("booking/book")
    public BookingResponse bookHotel(@Valid @RequestBody BookingRequest bookingRequest) throws Exception {
        return bookingManagementInterface.bookHotel(bookingRequest);
    }

    @GetMapping("booking/getAllDetails")
    public List<BookingResponse> getAllBookingDetails()throws Exception{
    return bookingManagementInterface.getAllBookingDetails();
    }

    @PutMapping("booking/update/{id}")
    public BookingResponse updateBooking(@PathVariable Long id,@Valid  @RequestBody BookingRequest bookingRequest)throws Exception{
        return bookingManagementInterface.updateBooking(id,bookingRequest);
    }

    @DeleteMapping("booking/delete/{id}")
    public BookingResponse cancelBooking(@PathVariable Long id)throws Exception{
        return bookingManagementInterface.cancelBooking(id);
    }
    @GetMapping("booking/search")
    public List<BookingResponse> searchBooking (
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) throws Exception{
        return bookingManagementInterface.searchBooking(
                userId,
                hotelId,
                fromDate,
                toDate
        );
    }

}
