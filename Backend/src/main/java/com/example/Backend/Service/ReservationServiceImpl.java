package com.example.Backend.Service;

import com.example.Backend.Model.Reservation;
import com.example.Backend.Model.User;
import com.example.Backend.dto.ReservationDTO;
import com.example.Backend.Repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    private final ReservationRepository reservationRepository;

    public List<ReservationDTO> getMyReservations(User user) {
        List<Reservation> reservations = reservationRepository.findByRequester(user);

        return reservations.stream().map(res -> new ReservationDTO(
                res.getId(),
                res.getStatus().name(),
                res.getRequestedAt(),
                res.getRespondedAt(),
                res.getSeatsRequested(),
                res.getAmount(),
                res.getTrip().getId(),
                res.getTrip().getOrigin().getAddress(),
                res.getTrip().getDestination().getAddress(),
                res.getTrip().getDriver().getName(),
                res.getTrip().getDriver().getId(),
                res.getTrip().getDepartureTime().toString(),
                res.getRequester().getId(),
                res.getRequester().getName()
        )).collect(Collectors.toList());
    }
}
