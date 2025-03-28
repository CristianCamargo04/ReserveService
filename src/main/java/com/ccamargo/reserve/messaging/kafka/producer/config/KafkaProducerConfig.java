package com.ccamargo.reserve.messaging.kafka.producer.config;

import com.ccamargo.reserve.model.reservation.ReservationDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaConfig getKafkaConfig(@Value("${spring.kafka.boostrap-servers}") String servers) {
        return KafkaConfig.builder()
                .boostrapServers(servers)
                .build();
    }

    @Bean
    public Producer<String, ReservationDTO> getProducer(KafkaConfig kafkaConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfig.getBoostrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer<>(props);
    }

}
