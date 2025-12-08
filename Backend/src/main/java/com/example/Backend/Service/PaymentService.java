package com.example.Backend.Service;

import com.example.Backend.Model.Payment;

import java.util.List;

public interface PaymentService {
    Payment process(Payment payment);

    Payment findById(Long id);
    Payment findByReservation(Long reservationId);
    List<Payment> findAll();
}
