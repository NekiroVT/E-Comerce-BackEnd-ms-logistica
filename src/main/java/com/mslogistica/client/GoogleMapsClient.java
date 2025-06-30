package com.mslogistica.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleMapsClient", url = "https://maps.googleapis.com")
public interface GoogleMapsClient {

    @GetMapping("/maps/api/geocode/json")
    String geocodeByAddress(
            @RequestParam("address") String address,
            @RequestParam("key") String apiKey
    );

    @GetMapping("/maps/api/geocode/json")
    String geocodeByLatLng(
            @RequestParam("latlng") String latlng,
            @RequestParam("key") String apiKey
    );

}
