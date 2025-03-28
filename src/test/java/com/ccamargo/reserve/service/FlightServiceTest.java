package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.FlightNotFoundException;
import com.ccamargo.reserve.exception.InvalidSeatNumberException;
import com.ccamargo.reserve.exception.SeatsUnavailableException;
import com.ccamargo.reserve.model.flight.FlightDTO;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.flight.FlightMapper;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @Mock
    private ValidateReservationService validateReservationService;

    @InjectMocks
    private FlightService flightService;

    private FlightEntity flightEntity;
    private FlightDTO flightDTO;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        flightEntity = new FlightEntity();
        flightEntity.setId(1L);
        flightEntity.setFlightNumber("ABC123");
        flightEntity.setTotalSeats(100);
        flightEntity.setAvailableSeats(10);

        flightDTO = new FlightDTO();
        flightDTO.setFlightNumber("ABC123");

        reservationDTO = new ReservationDTO();
        reservationDTO.setFlightId(1L);
        reservationDTO.setSeatNumber(5);
    }

    @Test
    void testGetFlightStatus_FlightExists() {
        when(flightRepository.findByFlightNumber("ABC123")).thenReturn(Optional.of(flightEntity));
        when(flightMapper.toFlightDTO(flightEntity)).thenReturn(flightDTO);

        FlightDTO result = flightService.getFlightStatus("ABC123");

        assertNotNull(result);
        assertEquals("ABC123", result.getFlightNumber());
        verify(flightRepository, times(1)).findByFlightNumber("ABC123");
    }

    @Test
    void testGetFlightStatus_FlightNotFound() {
        when(flightRepository.findByFlightNumber("XYZ789")).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightStatus("XYZ789"));
    }

    @Test
    void testValidateFlightAndSeat_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flightEntity));

        FlightEntity result = flightService.validateFlightAndSeat(reservationDTO);
        assertNotNull(result);
    }

    @Test
    void testValidateFlightAndSeat_FlightNotFound() {
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());
        reservationDTO.setFlightId(2L);
        assertThrows(FlightNotFoundException.class, () -> flightService.validateFlightAndSeat(reservationDTO));
    }

    @Test
    void testValidateSeatAvailability_NoSeatsAvailable() {
        flightEntity.setAvailableSeats(0);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flightEntity));
        assertThrows(SeatsUnavailableException.class, () -> flightService.validateFlightAndSeat(reservationDTO));
    }

    @Test
    void testValidateSeatAvailability_InvalidSeatNumber() {
        reservationDTO.setSeatNumber(0);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flightEntity));
        assertThrows(InvalidSeatNumberException.class, () -> flightService.validateFlightAndSeat(reservationDTO));
    }
}
