package com.ccamargo.reserve.messaging.kafka.consumer;

import com.ccamargo.reserve.messaging.kafka.producer.KafkaProducer;
import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import com.ccamargo.reserve.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class ReservationConsumer {

    private final ReservationService reservationService;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessage(ReservationDTO reservation) {
        try {
            log.info("reading message, {}, {}", reservation.getFlightId(), reservation.getSeatNumber());
            ReservationDTO reservationDTO = reservationService.bookSeat(reservation);
            log.info("The reservation has been successfully - {}", reservationDTO.getId());
            kafkaProducer.sendMessage(reservationDTO, Topic.SUCCESSFUL_BOOK);
        } catch (Exception e) {
           log.error("error processing message");
           log.error(e.getMessage());
        }
    }
}

