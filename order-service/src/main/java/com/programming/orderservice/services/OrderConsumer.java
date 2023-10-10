package com.programming.orderservice.services;

import com.programming.orderservice.dtos.OrderUpdateRabbitMQ;
import com.programming.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Slf4j
public class OrderConsumer {

    @Autowired
    private OrderService orderService;

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queues.update-order}")
    public void updateOrderConsumer(OrderUpdateRabbitMQ orderUpdateRabbitMQ) {
        log.info("UpdateOrderConsumer consumed {} from queue", orderUpdateRabbitMQ.getOrderId());
        orderService.updateOrderStatus(orderUpdateRabbitMQ.getOrderId(), OrderStatus.SUCCESSFUL);
    }
}