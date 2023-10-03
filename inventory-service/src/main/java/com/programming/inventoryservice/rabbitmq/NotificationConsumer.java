package com.programming.inventoryservice.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(UUID productId) {
        log.info("Consumed {} from queue", productId);
    }
}