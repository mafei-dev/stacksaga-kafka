package org.saga;

public interface SagaEvent<A extends SagaEvent<?>> {
    String name();

    default A done() {
        throw new UnsupportedOperationException();
    }
}
