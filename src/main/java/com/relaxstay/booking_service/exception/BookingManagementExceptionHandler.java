package com.relaxstay.booking_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingManagementExceptionHandler {

    @ExceptionHandler(BookingCancelledAldreadyException.class)
    public ResponseEntity<ErrorResponse> handleBookingCancelledAldreadyException(BookingCancelledAldreadyException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .url(request.getRequestURI())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFoundException(BookingNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .url(request.getRequestURI())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InputNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInputNotFoundException(InputNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .url(request.getRequestURI())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientRoomsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientRoomsException(InsufficientRoomsException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .url(request.getRequestURI())
                .httpStatus(HttpStatus.CONFLICT.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
