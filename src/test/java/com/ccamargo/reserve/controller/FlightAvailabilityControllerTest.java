package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.flight.FlightDTO;
import com.ccamargo.reserve.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightAvailabilityControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightAvailabilityController flightAvailabilityController;

    private FlightDTO flightDTO;

    @BeforeEach
    void setUp() {
        flightDTO = new FlightDTO();
        flightDTO.setId(1L);
        flightDTO.setFlightNumber("AA123");
        flightDTO.setTotalSeats(180);
        flightDTO.setAvailableSeats(50);
        flightDTO.setReservations(Collections.emptyList());
    }

    @Test
    void testFlightStatus_Success() {
        when(flightService.getFlightStatus("AA123")).thenReturn(flightDTO);

        ResponseEntity<FlightDTO> response = flightAvailabilityController.flightStatus("AA123");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("AA123", response.getBody().getFlightNumber());
        assertEquals(180, response.getBody().getTotalSeats());
        assertEquals(50, response.getBody().getAvailableSeats());
        assertTrue(response.getBody().getReservations().isEmpty());
    }

    @Test
    void testFlightStatus_NotFound() {
        when(flightService.getFlightStatus(anyString())).thenThrow(new RuntimeException("Vuelo no encontrado"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightAvailabilityController.flightStatus("UNKNOWN");
        });

        assertEquals("Vuelo no encontrado", exception.getMessage());
    }
}
