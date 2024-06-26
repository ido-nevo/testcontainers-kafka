package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class KafkaConsumer {
    Consumer<String, String> consumer;

    public KafkaConsumer(String bootstrapServerConfig) {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProps);
    }

    public String consume() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(800));
        for (ConsumerRecord<String, String> record: records) {
            log.info("Received message: " + record.value());
            return record.value();
        }
        return null;
    }

    public void subscribe(String topicName) {
        consumer.subscribe(Collections.singletonList(topicName));
    }
}
