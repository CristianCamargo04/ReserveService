package com.ccamargo.reserve.repository;

import com.ccamargo.reserve.model.flight.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
