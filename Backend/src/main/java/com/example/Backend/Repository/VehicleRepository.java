package com.example.Backend.Repository;

import com.example.Backend.Model.Trip;
import com.example.Backend.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Trip,Long> {
}
