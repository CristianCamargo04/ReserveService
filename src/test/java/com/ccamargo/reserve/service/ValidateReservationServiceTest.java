package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.SeatAlreadyBookedException;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.model.reservation.ReservationEntity;
import com.ccamargo.reserve.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ValidateReservationService validateReservationService;

    private FlightEntity flightEntity;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        flightEntity = new FlightEntity();
        flightEntity.setId(1L);

        reservationDTO = new ReservationDTO();
        reservationDTO.setFlightId(1L);
        reservationDTO.setSeatNumber(10);
    }

    @Test
    void testFindReservationBySeatNumber_SeatAlreadyBooked() {
        when(reservationRepository.findBySeatNumberAndFlightId(10, 1L))
                .thenReturn(Optional.of(new ReservationEntity()));

        assertThrows(SeatAlreadyBookedException.class,
                () -> validateReservationService.findReservationBySeatNumber(flightEntity, reservationDTO));
    }

    @Test
    void testFindReservationBySeatNumber_SeatAvailable() {
        when(reservationRepository.findBySeatNumberAndFlightId(10, 1L))
                .thenReturn(Optional.empty());

        validateReservationService.findReservationBySeatNumber(flightEntity, reservationDTO);

        verify(reservationRepository, times(1)).findBySeatNumberAndFlightId(10, 1L);
    }
}
