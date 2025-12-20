package com.example.Backend.Controller;

import com.example.Backend.Model.Location;
import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import com.example.Backend.Repository.TripRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.Service.TripService;
import com.example.Backend.Service.TripServiceImpl;
import com.example.Backend.dto.TripDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripServiceImpl tripService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TripRepository tripRepository;

    public TripController(TripServiceImpl tripService) {

        this.tripService = tripService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTrip(@RequestBody Map<String, Object> tripMap, Principal principal) {
        try {
            String userEmail = String.valueOf(principal.getName());
            User user = userRepo.findByEmail(userEmail).orElseThrow();
            Long driverId = user.getId();

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

    @GetMapping("/my")
    public ResponseEntity<List<TripDTO>> getMyTrips(Principal principal) {
        String userEmail = String.valueOf(principal.getName());
        User user=userRepo.findByEmail(userEmail).orElseThrow();
        return ResponseEntity.ok(
                tripService.getMyTrips(user)
        );
    }
    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        return ResponseEntity.ok(
                tripService.getAllTrips()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (trip != null) {
            TripDTO tripDTO = new TripDTO(
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
            );

            return ResponseEntity.ok(tripDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

