package org.example.stacksagakafka.saga;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SagaStepExecutor {
    String rootTopic();
}
