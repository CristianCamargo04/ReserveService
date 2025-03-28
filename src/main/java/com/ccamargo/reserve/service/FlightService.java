package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.FlightNotFoundException;
import com.ccamargo.reserve.exception.InvalidSeatNumberException;
import com.ccamargo.reserve.exception.SeatsUnavailableException;
import com.ccamargo.reserve.model.flight.FlightDTO;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.flight.FlightMapper;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final ValidateReservationService validateReservationService;

    public FlightDTO getFlightStatus(String flightNumber) {
        Optional<FlightEntity> flight = flightRepository.findByFlightNumber(flightNumber);
        if (flight.isEmpty()) {
            throw new FlightNotFoundException("Vuelo no encontrado");
        }
        return flightMapper.toFlightDTO(flight.get());
    }

    public FlightEntity validateFlightAndSeat(ReservationDTO reservationDTO) {
        FlightEntity flight = findFlightbyId(reservationDTO.getFlightId());
        validateSeatAvailability(flight, reservationDTO);
        return flight;
    }

    private void validateSeatAvailability(FlightEntity flight, ReservationDTO dto) {
        if (flight.getAvailableSeats() <= 0) {
            throw new SeatsUnavailableException("No hay asientos disponibles");
        }

        if (dto.getSeatNumber() > flight.getTotalSeats() || dto.getSeatNumber() == 0) {
            throw new InvalidSeatNumberException("El número de asiento es inválido para el vuelo seleccionado");
        }

        validateReservationService.findReservationBySeatNumber(flight, dto);
    }

    public FlightEntity findFlightbyId(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Vuelo no encontrado"));
    }


}
