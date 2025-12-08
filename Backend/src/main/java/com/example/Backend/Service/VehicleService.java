package com.example.Backend.Service;

import com.example.Backend.Model.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);
    Vehicle update(Vehicle vehicle);
    void deleteById(Long id);

    Vehicle findById(Long id);
    List<Vehicle> findByOwner(Long userId);
    List<Vehicle> findAll();
}
