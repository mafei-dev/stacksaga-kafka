package org.example.stacksagakafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.stacksagakafka.Order;
import org.example.stacksagakafka.SagaAggregatorPayload;
import org.example.stacksagakafka.saga.PlaceOrderAggregator;
import org.example.stacksagakafka.saga.PlaceOrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping
public class TestController {
    private final KafkaTemplate<String, SagaAggregatorPayload> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TestController(KafkaTemplate<String, SagaAggregatorPayload> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public void test() {
        SagaAggregatorPayload payload = new SagaAggregatorPayload();
        PlaceOrderAggregator order = new PlaceOrderAggregator();
        payload.setRootTopic("saga_place_order");
        payload.setRecentTopic(PlaceOrderEvent.MAKE_PAYMENT.name());
        order.setOrderId(UUID.randomUUID().toString());
        payload.setPayload(objectMapper.valueToTree(order));

        Message<SagaAggregatorPayload> build = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, "saga_place_order")
                .setHeader("my-custom-header", "value123")
                .build();
        CompletableFuture<SendResult<String, SagaAggregatorPayload>> send = this.kafkaTemplate.send(build);
    }
}
