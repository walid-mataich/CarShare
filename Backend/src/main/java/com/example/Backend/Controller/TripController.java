package com.example.Backend.Controller;

import com.example.Backend.Model.Location;
import com.example.Backend.Model.Trip;
import com.example.Backend.Service.TripService;
import com.example.Backend.Service.TripServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripServiceImpl tripService;

    public TripController(TripServiceImpl tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody Map<String, Object> tripMap) {
        try {
            Long driverId = Long.valueOf(tripMap.get("driverId").toString());

            Map<String, Object> originMap = (Map<String, Object>) tripMap.get("origin");
            Map<String, Object> destinationMap = (Map<String, Object>) tripMap.get("destination");

            Location origin = Location.builder()
                    .lat(Double.parseDouble(originMap.get("lat").toString()))
                    .lng(Double.parseDouble(originMap.get("lng").toString()))
                    .address(originMap.get("address").toString())
                    .build();

            Location destination = Location.builder()
                    .lat(Double.parseDouble(destinationMap.get("lat").toString()))
                    .lng(Double.parseDouble(destinationMap.get("lng").toString()))
                    .address(destinationMap.get("address").toString())
                    .build();

            Instant departureTime = Instant.parse(tripMap.get("departureTime").toString());
            int seats = Integer.parseInt(tripMap.get("availableSeats").toString());
            double price = Double.parseDouble(tripMap.get("price").toString());
            Trip savedTrip = tripService.createTrip(driverId, origin, destination, departureTime, seats, price);

            return ResponseEntity.ok(savedTrip);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid departureTime format. Use ISO-8601.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

