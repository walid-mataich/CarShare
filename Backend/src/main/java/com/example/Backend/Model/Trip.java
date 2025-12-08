package com.example.Backend.Model;



import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id")
    private Location origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Location destination;


    private Instant departureTime;
    private int availableSeats;
    private double price;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Message> relatedMessages;

    public Trip createTrip() { return this; }
    public Trip updateTrip() { return this; }
    public void cancelTrip() { /* set flag / notify */ }
}





