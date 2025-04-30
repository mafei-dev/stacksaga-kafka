package org.example.stacksagakafka;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class KafkaListenerAspect {

    private final KafkaTemplate<String, SagaAggregatorPayload> kafkaTemplate;

    public KafkaListenerAspect(KafkaTemplate<String, SagaAggregatorPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Pointcut("execution(public * org.example.stacksagakafka.SagaEndpoint+.*(..))")
    public void kafkaListenerMethods() {
    }

    @Around("kafkaListenerMethods()")
    public Object aroundKafkaListenerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // Perform logic before method execution
        System.out.println("Before KafkaListener execution");
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (joinPoint.getTarget() instanceof SagaEndpoint) {
            System.out.println("SagaEndpoint");
            if (joinPoint.getSignature().getName().equals("action")) {
                final SagaAggregatorPayload payload = (SagaAggregatorPayload) joinPoint.getArgs()[0];
                final Object result = joinPoint.proceed(joinPoint.getArgs());
                this.kafkaTemplate.send(payload.getRootTopic(), payload);
                return result;
            } else if (joinPoint.getSignature().getName().equals("reaction")) {
                throw new RuntimeException("Unrecognized Method");
            } else {
                throw new RuntimeException("Unrecognized Method");
            }

        }
        return joinPoint.proceed(joinPoint.getArgs()); // Optionally return a modified result
    }
}