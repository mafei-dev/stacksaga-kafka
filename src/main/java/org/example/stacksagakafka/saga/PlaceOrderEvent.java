package org.example.stacksagakafka.saga;

import org.saga.SagaEvent;

public enum PlaceOrderEvent implements SagaEvent<PlaceOrderEvent> {
    INIT_ORDER,
    MAKE_PAYMENT,
    UPDATE_STOCK,
    REVERT_MAKE_PAYMENT,
    REVERT_MAKE_PAYMENT_SUB_BEFORE_1,
    REVERT_MAKE_PAYMENT_SUB_BEFORE_2,
    REVERT_MAKE_PAYMENT_SUB_AFTER_1,
    REVERT_MAKE_PAYMENT_SUB_AFTER_2,
    REVERT_UPDATE_STOCK,
    ;
}
