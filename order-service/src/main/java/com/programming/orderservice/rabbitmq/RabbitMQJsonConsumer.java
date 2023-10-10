package com.programming.orderservice.rabbitmq;

import com.programming.orderservice.dtos.OrderUpdateRabbitMQ;
import com.programming.orderservice.enums.OrderStatus;
import com.programming.orderservice.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQJsonConsumer {

    @Autowired
    private OrderService orderService;

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(OrderUpdateRabbitMQ orderUpdateRabbitMQ) {
        log.info("Consumed {} from queue", orderUpdateRabbitMQ.getOrderId());
        orderService.updateOrderStatus(orderUpdateRabbitMQ.getOrderId(), OrderStatus.SUCCESSFUL);
    }
}