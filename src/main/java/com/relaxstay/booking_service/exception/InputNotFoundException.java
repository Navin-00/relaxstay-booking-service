package com.relaxstay.booking_service.exception;

public class InputNotFoundException extends RuntimeException{
    public InputNotFoundException(String message){
        super(
                message
        );
    }
}
