package com.example.Backend.Service;

import com.example.Backend.Model.Location;
import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import com.example.Backend.Repository.LocationRepository;
import com.example.Backend.Repository.TripRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.TripDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Backend.Repository.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private final TripRepository tripRepository;
    @Autowired
    private final LocationRepository locationRepository;
    @Autowired
    private final UserRepository userRepository;

    public TripServiceImpl(TripRepository tripRepository, LocationRepository locationRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Trip createTrip(Long driverId, Location origin, Location destination, Instant departureTime, int seats, double price) {

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Save locations first
        Location savedOrigin = locationRepository.save(origin);
        Location savedDestination = locationRepository.save(destination);

        Trip trip = Trip.builder()
                .driver(driver)
                .origin(savedOrigin)
                .destination(savedDestination)
                .departureTime(departureTime)
                .availableSeats(seats)
                .placeRestant(seats)
                .price(price)
                .build();

        return tripRepository.save(trip);
    }
    public List<TripDTO> getMyTrips(User user) {
        List<Trip> trips = tripRepository.findByDriver(user);

        return trips.stream().map(trip -> new TripDTO(
                trip.getId(),
                trip.getOrigin().getAddress(),
                trip.getOrigin().getLat(),
                trip.getOrigin().getLng(),
                trip.getDestination().getAddress(),
                trip.getDestination().getLat(),
                trip.getDestination().getLng(),
                trip.getDriver().getName(),
                trip.getDepartureTime().toString(),
                trip.getPrice(),
                trip.getAvailableSeats(),
                trip.getPlaceRestant()
        )).collect(Collectors.toList());

    }
    public List<TripDTO> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();

        return trips.stream().map(trip -> new TripDTO(
                trip.getId(),
                trip.getOrigin().getAddress(),
                trip.getOrigin().getLat(),
                trip.getOrigin().getLng(),
                trip.getDestination().getAddress(),
                trip.getDestination().getLat(),
                trip.getDestination().getLng(),
                trip.getDriver().getName(),
                trip.getDepartureTime().toString(),
                trip.getPrice(),
                trip.getAvailableSeats(),
                trip.getPlaceRestant()
        )).collect(Collectors.toList());

    }
}
