package com.example.Backend.Service;



import com.example.Backend.Model.Trip;
import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip createTrip(Trip trip);
    Optional<Trip> findById(Long id);
    Trip updateTrip(Trip trip);
    void cancelTrip(Long id);
    List<Trip> findByDriver(Long driverId);
}

