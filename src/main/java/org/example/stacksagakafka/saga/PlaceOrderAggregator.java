package org.example.stacksagakafka.saga;

import org.saga.SagaAggregator;

public class PlaceOrderAggregator extends SagaAggregator {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
