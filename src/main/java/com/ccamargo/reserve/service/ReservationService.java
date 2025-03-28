package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.*;
import com.ccamargo.reserve.messaging.kafka.producer.KafkaProducer;
import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.model.reservation.ReservationEntity;
import com.ccamargo.reserve.model.reservation.ReservationMapper;
import com.ccamargo.reserve.repository.FlightRepository;
import com.ccamargo.reserve.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService  {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final ReservationMapper reservationMapper;
    private final KafkaProducer kafkaProducer;
    private final FlightService flightService;

    @Transactional
    public void processReservation(ReservationDTO reservationDTO) {
        flightService.validateFlightAndSeat(reservationDTO);
        kafkaProducer.sendMessage(reservationDTO, Topic.BOOK_SEAT);
    }

    @Transactional
    public ReservationDTO bookSeat(ReservationDTO reservationDTO) {
        FlightEntity flight = flightService.validateFlightAndSeat(reservationDTO);

        ReservationEntity reservation = reservationMapper.toReservation(reservationDTO);
        reservation.setFlight(flight);

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);
        reservationRepository.save(reservation);

        return reservationMapper.toReservationDTO(reservation);
    }

    @Transactional
    public void cancelReservation(ReservationDTO dto) {
        ReservationEntity reservation = reservationRepository.findById(dto.getId())
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada con ID: " + dto.getId()));
        FlightEntity flightEntity = reservation.getFlight();
        flightEntity.setAvailableSeats(flightEntity.getAvailableSeats() + 1);
        flightRepository.save(flightEntity);
        reservationRepository.deleteById(reservation.getId());
    }

    @Transactional
    public List<ReservationDTO> listReservationsByFlight(Long flightId) {
        flightService.findFlightbyId(flightId);
        List<ReservationEntity> reservations = reservationRepository.findByFlightId(flightId);
        return reservationMapper.toListReservationDTO(reservations);
    }

}