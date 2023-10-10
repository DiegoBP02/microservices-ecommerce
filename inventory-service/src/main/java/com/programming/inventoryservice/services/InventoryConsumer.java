package com.programming.inventoryservice.services;

import com.programming.inventoryservice.dtos.InventoryRequest;
import com.programming.inventoryservice.dtos.InventoryUpdateRabbitMQ;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryConsumer {

    @Autowired
    private InventoryService inventoryService;

    @RabbitListener(queues = "${rabbitmq.queues.create-inventory}")
    public void createInventoryConsumer(InventoryRequest inventoryRequest) {
        log.info("CreateInventoryConsumer consumed {} from queue", inventoryRequest);
        inventoryService.create(inventoryRequest);
    }

    @RabbitListener(queues = "${rabbitmq.queues.update-inventory}")
    public void updateInventoryConsumer(InventoryUpdateRabbitMQ inventoryUpdateRabbitMQ) {
        log.info("UpdateInventoryConsumer consumed {} from queue", inventoryUpdateRabbitMQ);
        inventoryService.updateProductsFromInventory(inventoryUpdateRabbitMQ);
    }
}