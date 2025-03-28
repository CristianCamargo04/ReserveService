package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookSeat_ShouldReturnNoContent() {
        ReservationDTO request = new ReservationDTO();

        ResponseEntity<Void> response = reservationController.bookSeat(request);

        assertEquals(204, response.getStatusCodeValue());
        verify(reservationService, times(1)).processReservation(request);
    }

    @Test
    void cancelReservation_ShouldReturnNoContent() {
        ReservationDTO request = new ReservationDTO();

        ResponseEntity<Void> response = reservationController.cancelReservation(request);

        assertEquals(204, response.getStatusCodeValue());
        verify(reservationService, times(1)).cancelReservation(request);
    }

    @Test
    void getReservations_ShouldReturnListOfReservations() {
        Long flightId = 1L;
        List<ReservationDTO> reservations = List.of(new ReservationDTO(), new ReservationDTO());
        when(reservationService.listReservationsByFlight(flightId)).thenReturn(reservations);

        ResponseEntity<List<ReservationDTO>> response = reservationController.getReservations(flightId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(reservationService, times(1)).listReservationsByFlight(flightId);
    }
}
