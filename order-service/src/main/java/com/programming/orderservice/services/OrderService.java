package com.programming.orderservice.services;

import com.programming.orderservice.dtos.OrderRequest;
import com.programming.orderservice.dtos.OrderUpdateRequest;
import com.programming.orderservice.dtos.ProductResponse;
import com.programming.orderservice.enums.OrderStatus;
import com.programming.orderservice.exceptions.ProductOutOfStockException;
import com.programming.orderservice.models.Order;
import com.programming.orderservice.models.OrderItem;
import com.programming.orderservice.repository.OrderRepository;
import com.programming.orderservice.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final String INVENTORY_SERVICE_URL = "http://INVENTORY-SERVICE/api/v1/inventory/";
    private final String PRODUCT_SERVICE_URL = "http://PRODUCT-SERVICE/api/v1/products/";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void create(OrderRequest orderRequest) {
        checkProductInStock(orderRequest.getOrderItemList());

        BigDecimal orderTotalAmount = getOrderTotalAmount(orderRequest.getOrderItemList());

        Order order = Order.builder()
                .orderItemList(orderRequest.getOrderItemList())
                .status(OrderStatus.PENDING)
                .totalAmount(orderTotalAmount)
                .build();

        orderRepository.save(order);
        log.info("A order was created. Order id: {}", order.getId());
    }

    private void checkProductInStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            String url = UriComponentsBuilder
                    .fromHttpUrl(INVENTORY_SERVICE_URL + "/isInStock")
                    .queryParam("productId", orderItem.getProductId())
                    .queryParam("quantity", orderItem.getQuantity())
                    .toUriString();

            boolean isInStock = Boolean.TRUE.equals(webClientBuilder.build().get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

            if (!isInStock) {
                Integer stockQuantity = checkStockQuantity(orderItem.getProductId());
                throw new ProductOutOfStockException("Product with ID " + orderItem.getProductId()
                        + " is out of stock. Current stock quantity: " + stockQuantity);
            }
        }
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

    private BigDecimal getOrderTotalAmount(List<OrderItem> orderItemList) {
        BigDecimal orderTotalAmount = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItemList) {
            String url = UriComponentsBuilder.fromHttpUrl(PRODUCT_SERVICE_URL + "/{productId}")
                    .buildAndExpand(orderItem.getProductId())
                    .toUriString();

            ProductResponse productResponse = webClientBuilder.build().get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();

            System.out.println("price: " + productResponse.getPrice());
            System.out.println("quantity: " + orderItem.getQuantity());

            BigDecimal itemTotal = productResponse.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderTotalAmount = orderTotalAmount.add(itemTotal);
        }

        return orderTotalAmount;
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
//        order.setQuantity(orderUpdateRequest.getQuantity());
    }

    public void updateOrderStatus(UUID id,OrderStatus orderStatus) {
        try {
            Order order = orderRepository.getReferenceById(id);
            order.setStatus(orderStatus);

            orderRepository.save(order);

            log.info("Status of order with id {} was updated to {}", order.getId(), order.getStatus());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(UUID id) {
        orderRepository.deleteById(id);
    }
}
