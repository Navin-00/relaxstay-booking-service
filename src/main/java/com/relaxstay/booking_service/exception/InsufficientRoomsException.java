package com.relaxstay.booking_service.exception;

import jakarta.persistence.criteria.CriteriaBuilder;

public class InsufficientRoomsException extends RuntimeException{
    public InsufficientRoomsException(String message){
        super(message);
    }
}
