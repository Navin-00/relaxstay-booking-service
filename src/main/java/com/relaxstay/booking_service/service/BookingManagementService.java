package com.relaxstay.booking_service.service;

import com.relaxstay.booking_service.entity.Booking;
import com.relaxstay.booking_service.enumeration.BookingStatus;
import com.relaxstay.booking_service.exception.BookingCancelledAldreadyException;
import com.relaxstay.booking_service.exception.BookingNotFoundException;
import com.relaxstay.booking_service.exception.InputNotFoundException;
import com.relaxstay.booking_service.exception.InsufficientRoomsException;
import com.relaxstay.booking_service.model.BookingRequest;
import com.relaxstay.booking_service.model.BookingResponse;
import com.relaxstay.booking_service.model.HotelRequest;
import com.relaxstay.booking_service.model.HotelResponse;
import com.relaxstay.booking_service.model.UserResponse;
import com.relaxstay.booking_service.producer.HotelUpdateRequestProducer;
import com.relaxstay.booking_service.repository.BookingManagementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingManagementService implements BookingManagementInterface{
    public final BookingManagementRepository bookingManagementRepository;
    private final ServiceCaller serviceCaller;
    private final HotelUpdateRequestProducer hotelUpdateRequestProducer;

    @Override
    public List<BookingResponse> getAllBookingDetails() {
        List<Booking> savedBookingLists=bookingManagementRepository.findAll();

        return savedBookingLists.stream().map(this::getBookingResponse
        ).collect(Collectors.toList());
    }

    @Override
    public BookingResponse bookHotel(BookingRequest bookingRequest) {
        if (bookingRequest.getHotelId() == null ||
                bookingRequest.getUserId() == null ||
                bookingRequest.getCheckInDate() == null ||
                bookingRequest.getCheckOutDate() == null ||
                bookingRequest.getNumberOfRooms() == null) {

        throw new InputNotFoundException("Input is not given");
        }
        // call user service
        UserResponse userResponse = serviceCaller.fetchUserDetails(bookingRequest.getUserId());
        // call hotel service
        HotelResponse hotelResponse = serviceCaller.fetchHotelDetails(bookingRequest.getHotelId());
        //check out and check in date error
        if (!bookingRequest.getCheckOutDate().isAfter(bookingRequest.getCheckInDate())) {
            throw new InputNotFoundException(
                    "Check-out date must be after check-in date"
            );
        }

        if (bookingRequest.getCheckInDate().isBefore(hotelResponse.getFromAvailableDate())
            || bookingRequest.getCheckOutDate().isAfter(hotelResponse.getToAvailableDate()))
        {
            throw new InputNotFoundException("Hotel is not available for the selected dates");
        }
            //rooms not available
            if (hotelResponse.getNoOfRoomsAvailable()
                    < bookingRequest.getNumberOfRooms()) {
                throw new InsufficientRoomsException("Not enough rooms available");
            }

            Booking booking = Booking.builder()
                    .userId(Long.valueOf(userResponse.getId()))
                    .hotelId(Long.valueOf(hotelResponse.getId()))
                    .checkInDate(bookingRequest.getCheckInDate())
                    .checkOutDate(bookingRequest.getCheckOutDate())
                    .numberOfRooms(bookingRequest.getNumberOfRooms())
                    .status(BookingStatus.CONFIRMED)
                    .totalAmount(
                                    bookingRequest.getNumberOfRooms()
                                    * hotelResponse.getPrice()
                                    * (int) countNoOfStayDays(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())
                    ).build();
           Booking savedBookingDetails = bookingManagementRepository.save(booking);
           // call update hotel API
            HotelRequest hotelRequest = HotelRequest.builder()
                    .name(hotelResponse.getName())
                    .place(hotelResponse.getPlace())
                    .noOfRoomsAvailable(hotelResponse.getNoOfRoomsAvailable()-booking.getNumberOfRooms())
                    .fromAvailableDate(String.valueOf(hotelResponse.getFromAvailableDate()))
                    .toAvailableDate(String.valueOf(hotelResponse.getToAvailableDate()))
                    .price(hotelResponse.getPrice())
                    .build();

            serviceCaller.updateHotelRoomCount(Long.valueOf(hotelResponse.getId()),hotelRequest);

            return getBookingResponse(savedBookingDetails);
    }

    @Override
    public BookingResponse updateBooking(Long id,BookingRequest bookingRequest) {

        Booking bookingDetails=bookingManagementRepository.findById(id).orElseThrow(()-> new BookingNotFoundException("Booking not Found"));
        //calling the hotelService
        HotelResponse hotelResponse = serviceCaller.fetchHotelDetails(bookingRequest.getHotelId()!=null?bookingRequest.getHotelId():bookingDetails.getHotelId());

        //rooms not available
        if(bookingRequest.getNumberOfRooms()> hotelResponse.getNoOfRoomsAvailable()){
            throw new InsufficientRoomsException("Room is not Available");
        }
        //cannot update cancelled booking
        if (bookingDetails.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingCancelledAldreadyException(
                    "Cancelled booking cannot be updated"
            );
        }

        long stayDays = countNoOfStayDays(
                bookingRequest.getCheckInDate()!=null?bookingRequest.getCheckInDate():bookingDetails.getCheckInDate(),
                bookingRequest.getCheckOutDate()!=null?bookingRequest.getCheckOutDate():bookingDetails.getCheckOutDate());

        double totalAmount = bookingRequest.getNumberOfRooms() * hotelResponse.getPrice() * stayDays;

        Booking bookingUpdateDetails=Booking.builder()
                .id(bookingDetails.getId())
                .userId((bookingRequest.getUserId())!=null? bookingRequest.getUserId() :bookingDetails.getUserId())
                .hotelId((bookingRequest.getHotelId())!=null? bookingRequest.getHotelId() :bookingDetails.getHotelId())
                .checkInDate((bookingRequest.getCheckInDate())!=null?bookingRequest.getCheckInDate():bookingDetails.getCheckInDate())
                .checkOutDate((bookingRequest.getCheckOutDate())!=null?bookingRequest.getCheckOutDate():bookingDetails.getCheckOutDate())
                .numberOfRooms((bookingRequest.getNumberOfRooms())!=null?bookingRequest.getNumberOfRooms():bookingDetails.getNumberOfRooms())
                .status(bookingDetails.getStatus())
                .totalAmount((int) totalAmount)
                .build();

        Booking savedUpdateBooking=bookingManagementRepository.save(bookingUpdateDetails);
        return getBookingResponse(savedUpdateBooking);
    }

    @Override
    public BookingResponse cancelBooking(Long id) {
        Booking bookingDetails=bookingManagementRepository.findById(id).orElseThrow(()->new BookingNotFoundException("No booking was found on this id"));
        if(bookingDetails.getStatus()==BookingStatus.CANCELLED){
            throw new BookingCancelledAldreadyException("Booking has been cancelled already");
        }
        else {
            Booking cancelBookingDetails = Booking.builder()
                    .id(id)
                    .userId(bookingDetails.getUserId())
                    .hotelId(bookingDetails.getHotelId())
                    .numberOfRooms(bookingDetails.getNumberOfRooms())
                    .checkInDate(bookingDetails.getCheckInDate())
                    .checkOutDate(bookingDetails.getCheckOutDate())
                    .totalAmount(bookingDetails.getTotalAmount())
                    .status(BookingStatus.CANCELLED)
                    .build();
            bookingManagementRepository.save(cancelBookingDetails);

            // TODO: push hotel request into topic
            hotelUpdateRequestProducer.sendBooking(cancelBookingDetails);
        return getBookingResponse(cancelBookingDetails);
        }
    }

    @Override
    public List<BookingResponse> searchBooking(Long userId, Long hotelId, LocalDate fromDate, LocalDate toDate)
    {
        if (userId != null) {
            serviceCaller.fetchUserDetails(userId);
        }
        if (hotelId != null) {
            serviceCaller.fetchHotelDetails(hotelId);
        }

        List<Booking> bookings = bookingManagementRepository.searchBookings(userId, hotelId, fromDate, toDate);
        return bookings.stream()
                .map(this::getBookingResponse).toList();
    }



    private long countNoOfStayDays(LocalDate checkInDate, LocalDate checkOutDate) {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public BookingResponse getBookingResponse(Booking savedBookingDetails){

        HotelResponse hotelResponse = serviceCaller.fetchHotelDetails(savedBookingDetails.getHotelId());
        //call userService
        UserResponse userResponse = serviceCaller.fetchUserDetails(savedBookingDetails.getUserId());
       return BookingResponse.builder()
                .id(savedBookingDetails.getId())
                .userId(savedBookingDetails.getUserId())
                .userName(userResponse.getName())
                .hotelId(savedBookingDetails.getHotelId())
                .hotelName(hotelResponse.getName())
                .checkInDate(savedBookingDetails.getCheckInDate())
                .checkOutDate(savedBookingDetails.getCheckOutDate())
                .hotelPlace(hotelResponse.getPlace())
                .numberOfRooms(savedBookingDetails.getNumberOfRooms())
                .totalAmount(savedBookingDetails.getTotalAmount())
                .status(savedBookingDetails.getStatus()).build();
    }
}
