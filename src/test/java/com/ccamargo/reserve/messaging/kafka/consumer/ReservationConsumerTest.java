package com.ccamargo.reserve.messaging.kafka.consumer;

import com.ccamargo.reserve.messaging.kafka.producer.KafkaProducer;
import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationConsumerTest.class);

    @Mock
    private ReservationService reservationService;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ReservationConsumer reservationConsumer;

    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        reservationDTO.setFlightId(100L);
        reservationDTO.setSeatNumber(12);
    }

    @Test
    void testConsumeMessage_SuccessfulBooking() {
        when(reservationService.bookSeat(reservationDTO)).thenReturn(reservationDTO);

        reservationConsumer.consumeMessage(reservationDTO);

        verify(reservationService, times(1)).bookSeat(reservationDTO);
        verify(kafkaProducer, times(1)).sendMessage(reservationDTO, Topic.SUCCESSFUL_BOOK);
    }

    @Test
    void testConsumeMessage_ExceptionHandling() {
        doThrow(new RuntimeException("Test Exception"))
                .when(reservationService).bookSeat(reservationDTO);

        reservationConsumer.consumeMessage(reservationDTO);

        verify(reservationService, times(1)).bookSeat(reservationDTO);
        verify(kafkaProducer, never()).sendMessage(any(), any());
    }
}
