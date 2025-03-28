package com.ccamargo.reserve.repository;

import com.ccamargo.reserve.model.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByFlightId(Long flightId);

    Optional<ReservationEntity> findBySeatNumberAndFlightId(int seatNumber, Long flightId);
}
