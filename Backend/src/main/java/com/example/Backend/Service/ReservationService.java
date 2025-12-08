package com.example.Backend.Service;

import com.example.Backend.Model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation r);
    Optional<Reservation> findById(Long id);
    Reservation acceptReservation(Long id);
    Reservation rejectReservation(Long id);
    List<Reservation> findByRequester(Long userId);
}