package com.example.Backend.Model;


import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Instant paidAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public void processPayment() {
        // Appel à un service de paiement externe → mise à jour du status
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = Instant.now();
    }
}
