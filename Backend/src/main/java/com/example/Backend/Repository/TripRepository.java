package com.example.Backend.Repository;

import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip,Long>{
}
