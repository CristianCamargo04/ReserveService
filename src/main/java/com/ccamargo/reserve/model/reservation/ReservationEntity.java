package com.ccamargo.reserve.model.reservation;

import com.ccamargo.reserve.model.flight.FlightEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonBackReference
    private FlightEntity flight;
}

