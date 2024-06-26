package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.testcontainers.containers.KafkaContainer;

import java.util.Properties;

@Slf4j
public class KafkaProducer {
    org.apache.kafka.clients.producer.KafkaProducer<String, String> procucer;

    public KafkaProducer(KafkaContainer kafkaContainer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        procucer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    public void send(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>("rawdata", msg);
        log.info("Sending message: " + record.value());
        procucer.send(record);
    }
}
