package com.ccamargo.reserve.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleFlightNotFoundException_ShouldReturnNotFound() {
        FlightNotFoundException exception = new FlightNotFoundException("Flight not found");
        ResponseEntity<String> response = globalExceptionHandler.handleFlightNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void handleSeatsUnavailableException_ShouldReturnConflict() {
        SeatsUnavailableException exception = new SeatsUnavailableException("Seats unavailable");
        ResponseEntity<String> response = globalExceptionHandler.handleSeatsUnavailableException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Seats unavailable", response.getBody());
    }

    @Test
    void handleInvalidSeatNumberException_ShouldReturnBadRequest() {
        InvalidSeatNumberException exception = new InvalidSeatNumberException("Invalid seat number");
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidSeatNumberException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid seat number", response.getBody());
    }

    @Test
    void handleSeatAlreadyBookedException_ShouldReturnConflict() {
        SeatAlreadyBookedException exception = new SeatAlreadyBookedException("Seat already booked");
        ResponseEntity<String> response = globalExceptionHandler.handleSeatAlreadyBookedException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Seat already booked", response.getBody());
    }

    @Test
    void handleReservationNotFoundException_ShouldReturnNotFound() {
        ReservationNotFoundException exception = new ReservationNotFoundException("Reservation not found");
        ResponseEntity<String> response = globalExceptionHandler.handleReservationNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Reservation not found", response.getBody());
    }

    @Test
    void handleEmailExistsException_ShouldReturnConflict() {
        EmailExistsException exception = new EmailExistsException("Email already exists");
        ResponseEntity<String> response = globalExceptionHandler.handleEmailExistsException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void handleLoginFailedException_ShouldReturnConflict() {
        LoginFailedException exception = new LoginFailedException("Login failed");
        ResponseEntity<String> response = globalExceptionHandler.handleLoginFailedException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }
}
