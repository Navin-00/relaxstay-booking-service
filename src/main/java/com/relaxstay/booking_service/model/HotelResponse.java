package com.relaxstay.booking_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Integer id;
    private String name;
    private String place;
    private  Integer price;
    private Integer noOfRoomsAvailable;
    private LocalDate fromAvailableDate;
    private LocalDate toAvailableDate;
}
