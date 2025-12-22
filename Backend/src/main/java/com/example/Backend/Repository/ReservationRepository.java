package com.example.Backend.Repository;

import com.example.Backend.Model.Reservation;
import com.example.Backend.Model.ReservationStatus;
import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer>{
    List<Reservation> findByRequester(User user);
    List<Reservation> findByTripId(Long tripId);
    Optional<Reservation> findById(long integer);
    List<Reservation> findByTripIdAndStatusNot(Long tripId, ReservationStatus status);
}
