package com.example.Backend.DTO;

import com.example.Backend.dto.UserDTO;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationTripDTO {

    private Long id;
    private String status;
    private Instant requestedAt;
    private Instant respondedAt;
    private int seatsRequested;
    private double amount;

    private Long tripID;
    private UserDTO requester;
}
