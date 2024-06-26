package com.example.stepDefinition;

import com.example.KafkaConsumer;
import com.example.KafkaProducer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.Calculator;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.assertEquals;

public class StepDefinitions {
    private final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
                    .withEmbeddedZookeeper()
                    .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLED", Boolean.TRUE.toString());
    private KafkaProducer producer;
    private KafkaConsumer consumer;

    Calculator calculator;

    @Given("Calculator service is initialized")
    public void calculatorServiceIsInitialized() {
        calculator = new Calculator();
    }

    @When("number {} is added with number {} and published to a kafka topic")
    public void numberIsAddedWithNumber(String a, String b) {
        var result = calculator.add(Integer.parseInt(a), Integer.parseInt(b));
        producer.send(String.valueOf(result));
    }


    @Then("Kafka consumes the result as {}")
    public void kafkaConsumeTheResultAs(String addRes) {
        String readFromTopic = consumer.consume();
        assertEquals(addRes, readFromTopic);
    }

    @Given("Kafka producer and consumer are initialized")
    public void initializationOfKafkaProducerAndConsumer() {
        kafka.start();
        producer = new KafkaProducer(kafka);
        final String bootstrapServerConfig = kafka.getBootstrapServers();
        consumer = new KafkaConsumer(bootstrapServerConfig);
        kafka.start();
        consumer.subscribe("rawdata");
    }
}
