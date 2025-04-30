package org.example.stacksagakafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.example.stacksagakafka.saga.PlaceOrderAggregator;
import org.example.stacksagakafka.saga.SagaStepExecutor;
import org.saga.SagaAggregator;
import org.saga.SagaEvent;
import org.saga.StepExecutor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class KafkaListenerConfig {

    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;
    public static final HashMap<String, Class<? extends StepExecutor<SagaAggregator, SagaEvent<?>>>> EXECUTORS_BY_TOPIC = new HashMap<>();

    public KafkaListenerConfig(BeanFactory beanFactory, ObjectMapper objectMapper) {
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public ConcurrentMessageListenerContainer<String, SagaAggregatorPayload> kafkaListenerContainer(ConsumerFactory<String, SagaAggregatorPayload> consumerFactory) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AssignableTypeFilter(StepExecutor.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("org.example");


        Set<String> topics = new HashSet<>();
        for (BeanDefinition bd : candidateComponents) {
            Class<?> clazz = Class.forName(bd.getBeanClassName());
            boolean annotationPresent = clazz.isAnnotationPresent(SagaStepExecutor.class);
            if (annotationPresent) {
                SagaStepExecutor annotation = clazz.getAnnotation(SagaStepExecutor.class);
                if (!topics.add(Objects.requireNonNull(annotation).rootTopic())) {
                    throw new RuntimeException("Topic " + annotation.rootTopic() + " already exists");
                } else {
                    EXECUTORS_BY_TOPIC.put(annotation.rootTopic(), (Class<? extends StepExecutor<SagaAggregator, SagaEvent<?>>>) clazz);
                }
            }
            System.out.println("Found implementation: " + clazz.getName());
        }
        ContainerProperties containerProps = new ContainerProperties(topics.toArray(String[]::new));
        containerProps.setMessageListener((MessageListener<String, SagaAggregatorPayload>) record -> {
            Class<? extends StepExecutor<?, ?>> bean = EXECUTORS_BY_TOPIC.get(record.topic());
            for (Type genericInterface : bean.getGenericInterfaces()) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type actualTypeArgument = parameterizedType.getActualTypeArguments()[1];
                String typeName = actualTypeArgument.getTypeName();
                try {
                    Class<?> aClass = Class.forName(typeName);
                    Object[] constants = aClass.getEnumConstants();
                    for (SagaEvent<?> obj : (SagaEvent<?>[]) constants) {
                        if (obj.toString().equals(record.value().getRecentTopic())) {
                            System.out.println("obj = " + obj);
                            StepExecutor<SagaAggregator, SagaEvent<?>> bean1 = (StepExecutor<SagaAggregator, SagaEvent<?>>) this.beanFactory.getBean(bean);
                            try {
                                Type actualTypeArgument1 = parameterizedType.getActualTypeArguments()[0];
                                Class<?> aClass1 = Class.forName(actualTypeArgument1.getTypeName());
                                Object o = this.objectMapper.treeToValue(record.value().getPayload(), aClass1);
                                bean1.onNext(obj, (SagaAggregator) o);
                            } catch (Exception e) {
//                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
                }
            }

        });
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }
}
