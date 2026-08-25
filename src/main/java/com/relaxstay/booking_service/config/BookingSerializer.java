package com.relaxstay.booking_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.relaxstay.booking_service.entity.Booking;
import org.apache.kafka.common.serialization.Serializer;

public class BookingSerializer implements Serializer<Booking> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BookingSerializer() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public byte[] serialize(String topic, Booking booking) {

        if (booking == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Booking", e);
        }
    }
}