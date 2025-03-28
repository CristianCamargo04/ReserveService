package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/book")
    @Operation(summary = "Reservar un asiento", description = "Permite solicitara la reserva de un asiento en un vuelo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva solicitada con éxito"),
            @ApiResponse(responseCode = "409", description = "El asiento ya está ocupado o no hay disponibilidad"),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    public ResponseEntity<Void> bookSeat(@RequestBody ReservationDTO request) {
        reservationService.processReservation(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cancel")
    @Operation(summary = "Cancelar una reserva", description = "Permite cancelar una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> cancelReservation(@RequestBody ReservationDTO request) {
        reservationService.cancelReservation(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Listar reservas de un vuelo", description = "Recupera todas las reservas asociadas a un vuelo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas recuperada con éxito"),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    public ResponseEntity<List<ReservationDTO>> getReservations(@PathVariable("flightId") Long flightId) {
        return ResponseEntity.ok(reservationService.listReservationsByFlight(flightId));
    }
}


