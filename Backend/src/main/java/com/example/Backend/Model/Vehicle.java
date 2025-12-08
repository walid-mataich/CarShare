package com.example.Backend.Model;



import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private String plateNumber;
    private int seats;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public void updateInfo(String make, String model, String plateNumber, int seats, String color) {
        this.make = make; this.model = model; this.plateNumber = plateNumber; this.seats = seats; this.color = color;
    }
}
