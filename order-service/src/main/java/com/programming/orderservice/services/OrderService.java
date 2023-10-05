package com.programming.orderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.orderservice.dtos.OrderRequest;
import com.programming.orderservice.dtos.OrderUpdateRequest;
import com.programming.orderservice.enums.OrderStatus;
import com.programming.orderservice.exceptions.ProductOutOfStockException;
import com.programming.orderservice.models.Order;
import com.programming.orderservice.repository.OrderRepository;
import com.programming.orderservice.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final String INVENTORY_SERVICE_URL = "http://INVENTORY-SERVICE/api/v1/inventory/";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void create(OrderRequest orderRequest) {
        boolean isInStock = checkProductInStock
                (orderRequest.getProductId(), orderRequest.getQuantity());

        if (!isInStock) {
            Integer stockQuantity = checkStockQuantity(orderRequest.getProductId());
            throw new ProductOutOfStockException("Product with ID " + orderRequest.getProductId()
                    + " is out of stock. Current stock quantity: " + stockQuantity);
        }

        Order order = Order.builder()
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);
    }

    private boolean checkProductInStock(UUID productId, int quantity) {
        String url = UriComponentsBuilder.fromHttpUrl(INVENTORY_SERVICE_URL + "/isInStock/{productId}")
                .queryParam("quantity", quantity)
                .buildAndExpand(productId)
                .toUriString();

        return Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }

    private Integer checkStockQuantity(UUID productId) {
        String url = UriComponentsBuilder.fromHttpUrl(INVENTORY_SERVICE_URL + "/stockQuantity/{productId}")
                .buildAndExpand(productId)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Order update(UUID id, OrderUpdateRequest orderUpdateRequest) {
        try {
            Order order = orderRepository.getReferenceById(id);
            updateData(order, orderUpdateRequest);

            return orderRepository.save(order);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Order order, OrderUpdateRequest orderUpdateRequest) {
        order.setQuantity(orderUpdateRequest.getQuantity());
    }

    public void delete(UUID id) {
        orderRepository.deleteById(id);
    }
}
