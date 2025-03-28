package com.ccamargo.reserve.messaging.kafka.producer;

import com.ccamargo.reserve.common.Topic;
import com.ccamargo.reserve.model.reservation.ReservationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class KafkaProducer {

    private final Producer<String, ReservationDTO> producer;

    public void sendMessage(ReservationDTO message, Topic topic) {
        log.info("send message reservation in process");
        producer.send(new ProducerRecord<>(topic.getTopicName(), message));
    }

}
