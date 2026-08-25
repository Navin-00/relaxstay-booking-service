package com.relaxstay.booking_service.producer;

import com.relaxstay.booking_service.config.KafkaProducerConfig;
import com.relaxstay.booking_service.entity.Booking;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HotelUpdateRequestProducer {

    @Value("${kafka.topic.hotel}")
    private String bookingTopic;

    private static final Logger log = LoggerFactory.getLogger(HotelUpdateRequestProducer.class);

    private final KafkaProducer<String, Booking> producer;

    public HotelUpdateRequestProducer() {
        this.producer = KafkaProducerConfig.createProducer();
    }

    public void sendBooking(Booking booking) {
        log.info("reached send booking");
        ProducerRecord<String, Booking> record =
                new ProducerRecord<>(
                        bookingTopic,
                        booking
                );

        producer.send(record, (metadata, exception) -> {
        log.info("producer sent the data");
            if (exception != null) {
                System.out.println(
                        "Failed to send message: "
                                + exception.getMessage()
                );
            } else {
                System.out.println(
                        "booking sent successfully"
                                + "\nTopic: " + metadata.topic()
                                + "\nPartition: " + metadata.partition()
                                + "\nOffset: " + metadata.offset()
                );
            }
        });
    }

    public void close() {
        producer.close();
    }
}