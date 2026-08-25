package com.relaxstay.booking_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequest {
    private String name;
    private String place;
    private Integer price;
    private Integer noOfRoomsAvailable;
    private String fromAvailableDate;
    private String toAvailableDate;
}

