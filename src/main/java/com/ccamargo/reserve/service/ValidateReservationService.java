package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.SeatAlreadyBookedException;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateReservationService {

    private final ReservationRepository reservationRepository;

    public void findReservationBySeatNumber(FlightEntity flight, ReservationDTO reservation) {
        reservationRepository.findBySeatNumberAndFlightId(reservation.getSeatNumber(), flight.getId())
                .ifPresent(reservationEntity -> {
                    throw new SeatAlreadyBookedException("El asiento " + reservation.getSeatNumber()
                            + " no está disponible para el vuelo " + reservation.getFlightId());
                });
    }
}
