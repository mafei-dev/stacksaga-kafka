package org.example.stacksagakafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.function.BiConsumer;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Retry: 2 retries with 1s delay each (total 3 attempts: 1 + 2)
        FixedBackOff backOff = new FixedBackOff(1000L, 2);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new ConsumerRecordRecoverer() {
            @Override
            public void accept(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
                System.out.println("exception = " + exception);
            }
        });
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            System.out.println("deliveryAttempt = " + deliveryAttempt);
        });
        return errorHandler;
    }

    @Bean("myListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> testContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }
}
