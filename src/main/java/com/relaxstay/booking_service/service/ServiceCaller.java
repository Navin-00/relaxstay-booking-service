package com.relaxstay.booking_service.service;

import com.relaxstay.booking_service.model.HotelRequest;
import com.relaxstay.booking_service.model.HotelResponse;
import com.relaxstay.booking_service.model.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceCaller {

    private final RestTemplate restTemplate;

    public ServiceCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserResponse fetchUserDetails(Long id) {
        String url = "http://localhost:8080/api/user/getUserDetailById/"+id;
        return restTemplate.getForObject(url, UserResponse.class);
    }

    public HotelResponse fetchHotelDetails(Long id) {
        String url = "http://localhost:8081/api/hotel/findById/"+id;
        return restTemplate.getForObject(url, HotelResponse.class);
    }

    public void updateHotelRoomCount(Long id, HotelRequest hotelRequest) {
        String url = "http://localhost:8081/api/hotel/update/"+id;
        restTemplate.put(url, hotelRequest);
    }
}
