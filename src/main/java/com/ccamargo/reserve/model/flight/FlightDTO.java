package com.ccamargo.reserve.model.flight;

import com.ccamargo.reserve.model.reservation.ReservationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private int totalSeats;
    private int availableSeats;
    private List<ReservationDTO> reservations;
}
