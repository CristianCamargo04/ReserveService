package com.ccamargo.reserve.service;

import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.exception.ReservationNotFoundException;
import com.ccamargo.reserve.messaging.kafka.producer.KafkaProducer;
import com.ccamargo.reserve.model.flight.FlightEntity;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.model.reservation.ReservationEntity;
import com.ccamargo.reserve.model.reservation.ReservationMapper;
import com.ccamargo.reserve.repository.FlightRepository;
import com.ccamargo.reserve.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationDTO reservationDTO;
    private ReservationEntity reservationEntity;
    private FlightEntity flightEntity;

    @BeforeEach
    void setUp() {
        flightEntity = new FlightEntity();
        flightEntity.setId(1L);
        flightEntity.setAvailableSeats(10);

        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        reservationDTO.setFlightId(1L);

        reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setFlight(flightEntity);
    }

    @Test
    void testProcessReservation() {
        when(flightService.validateFlightAndSeat(reservationDTO)).thenReturn(new FlightEntity());
        doNothing().when(kafkaProducer).sendMessage(reservationDTO, Topic.BOOK_SEAT);

        reservationService.processReservation(reservationDTO);

        verify(flightService, times(1)).validateFlightAndSeat(reservationDTO);
        verify(kafkaProducer, times(1)).sendMessage(reservationDTO, Topic.BOOK_SEAT);
    }


    @Test
    void testBookSeat() {
        when(flightService.validateFlightAndSeat(reservationDTO)).thenReturn(flightEntity);
        when(reservationMapper.toReservation(reservationDTO)).thenReturn(reservationEntity);
        when(reservationMapper.toReservationDTO(reservationEntity)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.bookSeat(reservationDTO);

        assertNotNull(result);
        verify(flightRepository, times(1)).save(flightEntity);
        verify(reservationRepository, times(1)).save(reservationEntity);
    }

    @Test
    void testCancelReservation_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservationEntity));

        reservationService.cancelReservation(reservationDTO);

        verify(flightRepository, times(1)).save(flightEntity);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCancelReservation_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ReservationNotFoundException.class, () -> reservationService.cancelReservation(reservationDTO));
    }

    @Test
    void testListReservationsByFlight() {
        when(flightService.findFlightbyId(1L)).thenReturn(flightEntity);
        when(reservationRepository.findByFlightId(1L)).thenReturn(List.of(reservationEntity));
        when(reservationMapper.toListReservationDTO(List.of(reservationEntity))).thenReturn(List.of(reservationDTO));

        List<ReservationDTO> result = reservationService.listReservationsByFlight(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByFlightId(1L);
    }
}
