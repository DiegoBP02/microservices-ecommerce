package com.programming.orderservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigOrder {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.update-order}")
    private String updateOrderQueue;

    @Value("${rabbitmq.routing-keys.update-order}")
    private String updateOrderRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue updateOrderQueue() {
        return new Queue(this.updateOrderQueue);
    }

    @Bean
    public Binding updateOrderBinding() {
        return BindingBuilder
                .bind(updateOrderQueue())
                .to(internalTopicExchange())
                .with(this.updateOrderRoutingKey);
    }

}