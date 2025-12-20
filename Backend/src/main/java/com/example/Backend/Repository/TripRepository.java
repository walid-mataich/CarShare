package com.example.Backend.Repository;

import com.example.Backend.Model.Trip;
import com.example.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip,Long>{
    List<Trip> findByDriver(User driver);

    @Override
    Optional<Trip> findById(Long aLong);
}
