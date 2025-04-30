package org.example.stacksagakafka.saga;

import org.saga.StepExecutor;
import org.springframework.stereotype.Component;

@SagaStepExecutor(rootTopic = "saga_place_order")
public class PlaceOrderStepExecutor implements StepExecutor<PlaceOrderAggregator, PlaceOrderEvent> {

    @Override
    public PlaceOrderEvent onNext(PlaceOrderEvent lastEvent, PlaceOrderAggregator aggregator) {
        System.out.println("lastEvent = " + lastEvent);
        return switch (lastEvent) {
            case INIT_ORDER -> PlaceOrderEvent.UPDATE_STOCK;
            case UPDATE_STOCK -> PlaceOrderEvent.MAKE_PAYMENT;
            default -> lastEvent.done();
        };
    }

    @Override
    public PlaceOrderEvent onNextRever(PlaceOrderEvent lastEvent, PlaceOrderAggregator aggregator) {
        return switch (lastEvent) {
            case REVERT_UPDATE_STOCK -> PlaceOrderEvent.REVERT_MAKE_PAYMENT_SUB_BEFORE_1;
            case REVERT_MAKE_PAYMENT_SUB_BEFORE_1 -> PlaceOrderEvent.REVERT_MAKE_PAYMENT_SUB_BEFORE_2;
            case REVERT_MAKE_PAYMENT_SUB_BEFORE_2 -> PlaceOrderEvent.REVERT_MAKE_PAYMENT;
            case REVERT_MAKE_PAYMENT -> PlaceOrderEvent.REVERT_MAKE_PAYMENT_SUB_AFTER_1;
            default -> lastEvent.done();
        };
    }
}
