package com.example.Backend.Repository;

import com.example.Backend.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Integer>{
}
