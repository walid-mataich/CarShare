package com.example.Backend.Model;



import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Instant createdAt;
    private boolean delivered;

    public void sendPush() {
        // intégration push (service externe)
        this.delivered = true;
    }
}
