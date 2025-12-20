package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    private String status;

    private Instant requestedAt;
    private Instant respondedAt;

    private int seatsRequested;
    private double amount;

    private Long tripId;
    private String tripOrigin;
    private String tripDestination;
    private String driverName;
    private String departureTime;

    private Long requesterId;
    private String requesterName;
}
