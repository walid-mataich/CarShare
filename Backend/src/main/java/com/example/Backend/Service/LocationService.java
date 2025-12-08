package com.example.Backend.Service;

import com.example.Backend.Model.Location;

public interface LocationService {
    Location geocode(String address);
    String reverseGeocode(double lat, double lng);
}
