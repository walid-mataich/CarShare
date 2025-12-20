package com.example.Backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long tripId;
    private int seatsRequested;
    private double prix;
}
