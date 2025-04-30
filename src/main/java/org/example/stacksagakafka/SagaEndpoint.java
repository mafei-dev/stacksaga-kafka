package org.example.stacksagakafka;

import java.util.Map;

public interface SagaEndpoint {
    void action(SagaAggregatorPayload payload, Map<String, Object> headers);

    void reaction(SagaAggregatorPayload payload, Map<String, Object> headers);
}
