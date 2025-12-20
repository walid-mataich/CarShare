package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    private Long id;

    private String originAddress;
    private double originLat;
    private double originLng;

    private String destinationAddress;
    private double destinationLat;
    private double destinationLng;

    private String driverName;
    private String departureTime; // LocalDateTime peut être utilisé si tu veux

    private double price;
    private int availableSeats;
    private int placeRestant;
}
