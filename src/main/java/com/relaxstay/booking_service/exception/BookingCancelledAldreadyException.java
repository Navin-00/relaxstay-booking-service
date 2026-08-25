package com.relaxstay.booking_service.exception;

public class BookingCancelledAldreadyException extends RuntimeException{
    public BookingCancelledAldreadyException(String message){
        super(message);
    }
}
