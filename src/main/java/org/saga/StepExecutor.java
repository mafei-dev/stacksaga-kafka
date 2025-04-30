package org.saga;

public interface StepExecutor<AG extends SagaAggregator, A extends SagaEvent<?>> {
    A onNext(A lastEvent, AG aggregator);

    default A onNextRever(A lastEvent, AG aggregator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
