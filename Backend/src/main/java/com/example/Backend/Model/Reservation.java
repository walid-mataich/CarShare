package com.example.Backend.Model;



import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Instant requestedAt;
    private Instant respondedAt;
    private int seatsRequested;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    public void request() { this.status = ReservationStatus.PENDING; this.requestedAt = Instant.now(); }
    public void accept() { this.status = ReservationStatus.ACCEPTED; this.respondedAt = Instant.now(); }
    public void reject() { this.status = ReservationStatus.REJECTED; this.respondedAt = Instant.now(); }
    public void cancel() { this.status = ReservationStatus.CANCELLED; this.respondedAt = Instant.now(); }
}
