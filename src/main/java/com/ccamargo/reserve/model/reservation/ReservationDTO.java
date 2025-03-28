package com.ccamargo.reserve.model.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private String passengerName;
    private int seatNumber;
    private Long flightId;
}
