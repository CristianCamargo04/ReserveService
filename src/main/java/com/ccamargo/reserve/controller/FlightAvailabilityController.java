package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.flight.FlightDTO;
import com.ccamargo.reserve.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class

FlightAvailabilityController {

    private final FlightService flightService;

    @GetMapping("/{flightNumber}")
    @Operation(
            summary = "Obtener el estado de un vuelo",
            description = "Recupera la información del vuelo basado en su número de vuelo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Información del vuelo recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlightDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vuelo no encontrado"
            )
    })
    public ResponseEntity<FlightDTO> flightStatus(@PathVariable("flightNumber") String flightNumber) {
        return ResponseEntity.ok(flightService.getFlightStatus(flightNumber));
    }
}
