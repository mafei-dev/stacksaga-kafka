package org.example.stacksagakafka.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.handler.annotation.Headers;
import org.stacksaga.async.kafka.CommandEndpoint;
import org.stacksaga.async.kafka.Endpoint;
import org.stacksaga.async.kafka.KafkaListener;
import org.stacksaga.async.kafka.SagaRetryableException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Endpoint(eventSuffix = "MAKE_DELIVERY")
public class MakeDeliveryEndpoint extends CommandEndpoint {

    @Override
    @KafkaListener("DO_MAKE_DELIVERY")
    public void doProcess(ConsumerRecord<String, JsonNode> aggregator, @Headers Map<String, Object> headers) throws SagaRetryableException {
        if (aggregator.value() instanceof ObjectNode node) {
            /*try {
                Thread.sleep(new Random().nextInt(0,2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            node.put("DO_MAKE_DELIVERY", LocalDateTime.now().toString());
        }
    }

    @Override
    @KafkaListener("UNDO_MAKE_DELIVERY")
    public void undoProcess(ConsumerRecord<String, JsonNode> aggregator, @Headers Map<String, Object> headers) throws SagaRetryableException {

    }


}
