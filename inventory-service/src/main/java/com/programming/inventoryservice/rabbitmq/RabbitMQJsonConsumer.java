package com.programming.inventoryservice.rabbitmq;

import com.programming.inventoryservice.dtos.InventoryRequest;
import com.programming.inventoryservice.services.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQJsonConsumer {

    @Autowired
    private InventoryService inventoryService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(InventoryRequest inventoryRequest) {
        log.info("Consumed {} from queue", inventoryRequest);
        inventoryService.create(inventoryRequest);
    }
}