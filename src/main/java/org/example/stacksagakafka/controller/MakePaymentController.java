package org.example.stacksagakafka.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.security.authenticator.DefaultKafkaPrincipalBuilder;
import org.example.stacksagakafka.SagaAggregatorPayload;
import org.example.stacksagakafka.SagaEndpoint;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MakePaymentController implements SagaEndpoint {

    @Override
//    @KafkaListener(topics = "order-created")
    public void action(@Payload SagaAggregatorPayload payload, @Headers Map<String, Object> headers) {
        System.out.println("payload = " + payload);
        System.out.println("headers = " + headers);
    }

    @Override
//    @KafkaListener(topics = "revert-order-created")
    public void reaction(@Payload SagaAggregatorPayload payload, @Headers Map<String, Object> headers) {
        System.out.println("revert:payload = " + payload);
        System.out.println("revert:headers = " + headers);
    }
}

