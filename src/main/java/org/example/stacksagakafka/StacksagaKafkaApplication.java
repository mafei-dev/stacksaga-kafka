package org.example.stacksagakafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.util.backoff.FixedBackOff;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

@SpringBootApplication
public class StacksagaKafkaApplication {

    /*
        @Bean
        public DefaultErrorHandler errorHandler() {
            ConsumerRecordRecoverer consumerRecordRecoverer = new ConsumerRecordRecoverer() {
                @Override
                public void accept(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
                    System.out.println("exception = " + exception);
                }
            };
            // No need to configure backoff here (it's handled by @RetryableTopic)
            return new DefaultErrorHandler(consumerRecordRecoverer);
        }

        *//*@Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        *//**//*recoverer.addHeadersFunction((consumerRecord, exception) -> {
            System.out.println("exception = " + exception);
            return null;
        });*//**//*
        FixedBackOff backOff = new FixedBackOff(1000L, 3L); // 3 retries, 1 sec apart

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
//                recoverer,
                (consumerRecord, exception) -> {
                    System.out.println("consumerRecord = " + consumerRecord);
                    System.out.println("consumerRecord:ex = " + exception.getMessage());
                    consumerRecord.headers().remove("x-retry-attempt");
                    consumerRecord.headers().add(new RecordHeader("x-retry-attempt", Integer.toString(1).getBytes()));
                    consumerRecord.headers().add(new RecordHeader("x-error", exception.getMessage().getBytes(StandardCharsets.UTF_8)));
                }, backOff);
        errorHandler.setRetryListeners(new RetryListener() {
            @Override
            public void failedDelivery(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
                System.out.println("StacksagaKafkaApplication.failedDelivery");
            }
        });
        return errorHandler;
    }*//*


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory
//            , RecordInterceptor<String, String> interceptor
            , DefaultErrorHandler errorHandler
//            RecordFilterStrategy<String, String> strategy
    ) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
//        factory.setConcurrency(3); // Number of concurrent threads
        factory.setCommonErrorHandler(errorHandler);
//        factory.setRecordFilterStrategy(strategy);
//        factory.setRecordInterceptor(interceptor);
        factory.setAutoStartup(true);

        return factory;
    }*/


    public static void main(String[] args) {
        SpringApplication.run(StacksagaKafkaApplication.class, args);
    }
}
