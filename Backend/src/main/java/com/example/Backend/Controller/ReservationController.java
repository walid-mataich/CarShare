package com.example.Backend.Controller;

import com.example.Backend.Model.Reservation;
import com.example.Backend.Model.ReservationStatus;
import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import com.example.Backend.Repository.ReservationRepository;
import com.example.Backend.Repository.TripRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.Service.ReservationService;
import com.example.Backend.Service.ReservationServiceImpl;
import com.example.Backend.dto.ReservationDTO;
import com.example.Backend.dto.ReservationRequest;
import com.example.Backend.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Backend.DTO.ReservationTripDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/user/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationServiceImpl reservationService;
    private final UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/my")
    public ResponseEntity<List<ReservationDTO>> getMyReservations(Principal principal) {
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        return ResponseEntity.ok(reservationService.getMyReservations(user));
    }
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        if (reservation.getStatus() == com.example.Backend.Model.ReservationStatus.PENDING ||
                reservation.getStatus() == com.example.Backend.Model.ReservationStatus.ACCEPTED) {

            reservation.cancel(); // méthode existante dans ta classe Reservation
            reservationRepository.save(reservation);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build(); // ne peut pas annuler si déjà rejetée, annulée ou terminée
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request,Principal principal) {
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip non trouvé"));

        String userEmail = principal.getName();
        User requester = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Reservation reservation = Reservation.builder()
                .trip(trip)
                .requester(requester)
                .seatsRequested(request.getSeatsRequested())
                .amount(request.getPrix())
                .status(ReservationStatus.PENDING)
                .requestedAt(Instant.now())
                .build();
        reservationRepository.save(reservation);
        return ResponseEntity.ok("Réservation créée avec succès");
    }
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ReservationTripDTO>> getReservationsByTrip(@PathVariable Long tripId) {

        List<ReservationTripDTO> reservations = reservationRepository
                .findByTripIdAndStatusNot(tripId, ReservationStatus.CANCELLED)
                .stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(reservations);
    }
    @PostMapping("/{reservationId}/accept")
    public ResponseEntity<?> acceptReservation(@PathVariable Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        Trip trip = reservation.getTrip();

        // ❌ Déjà acceptée
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            return ResponseEntity.badRequest().body("Réservation déjà traitée");
        }

        // ❌ Pas assez de places
        if (reservation.getSeatsRequested() > trip.getPlaceRestant()) {
            return ResponseEntity.badRequest().body("Places insuffisantes");
        }

        // ✅ Acceptation
        reservation.accept();
        trip.setPlaceRestant(trip.getPlaceRestant() - reservation.getSeatsRequested());

        reservationRepository.save(reservation);
        tripRepository.save(trip);

        return ResponseEntity.ok().build();
    }

    // 🔹 Mapping Entity → DTO
    private ReservationTripDTO mapToDTO(Reservation reservation) {
        return ReservationTripDTO.builder()
                .id(reservation.getId())
                .status(reservation.getStatus().name())
                .requestedAt(reservation.getRequestedAt())
                .respondedAt(reservation.getRespondedAt())
                .seatsRequested(reservation.getSeatsRequested())
                .amount(reservation.getAmount())
                .tripID(reservation.getTrip().getId())
                .requester(
                        UserDTO.builder()
                                .id(reservation.getRequester().getId())
                                .name(reservation.getRequester().getName())
                                .email(reservation.getRequester().getEmail())
                                .build()
                )
                .build();
    }
}

