package com.relaxstay.booking_service.config;

import com.relaxstay.booking_service.entity.Booking;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-server}")
    private static String kafkaServerUrl;

    public static KafkaProducer<String, Booking> createProducer() {

        Properties properties = new Properties();

        properties.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaServerUrl
        );

        properties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        properties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                BookingSerializer.class.getName()
        );

        return new KafkaProducer<>(properties);
    }
}
