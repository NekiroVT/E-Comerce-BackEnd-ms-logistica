package com.mslogistica.service;

import com.mslogistica.client.GoogleMapsClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final GoogleMapsClient googleMapsClient;

    @Value("${google.maps.api.key}")
    private String apiKey;

    public String getPostalCodeByLatLng(Double lat, Double lng) {
        String latlng = lat + "," + lng;
        String response = googleMapsClient.geocodeByLatLng(latlng, apiKey);
        return extractPostalCode(response);
    }

    public String getPostalCodeByAddress(String address) {
        String response = googleMapsClient.geocodeByAddress(address, apiKey);
        return extractPostalCode(response);
    }

    private String extractPostalCode(String response) {
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");
        if (results.length() > 0) {
            JSONArray components = results.getJSONObject(0).getJSONArray("address_components");
            for (int i = 0; i < components.length(); i++) {
                JSONObject comp = components.getJSONObject(i);
                JSONArray types = comp.getJSONArray("types");
                for (int j = 0; j < types.length(); j++) {
                    if (types.getString(j).equals("postal_code")) {
                        return comp.getString("long_name");
                    }
                }
            }
        }
        return null;
    }

}
