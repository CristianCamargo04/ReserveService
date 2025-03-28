package com.ccamargo.reserve.messaging.kafka.producer.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KafkaConfig {
    private String boostrapServers;
}
