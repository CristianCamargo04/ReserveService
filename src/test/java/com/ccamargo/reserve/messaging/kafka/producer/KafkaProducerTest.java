package com.ccamargo.reserve.messaging.kafka.producer;

import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private Producer<String, ReservationDTO> producer;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private ReservationDTO reservationDTO;
    private Topic topic;

    @BeforeEach
    void setUp() {
        reservationDTO = new ReservationDTO();
        topic = Topic.BOOK_SEAT;
    }

    @Test
    void sendMessage_ShouldSendRecordToKafka() {
        kafkaProducer.sendMessage(reservationDTO, topic);

        verify(producer, times(1)).send(any(ProducerRecord.class));
    }
}
