package com.programming.orderservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.programming.orderservice.dtos.OrderRequest;
import com.programming.orderservice.dtos.OrderUpdateRequest;
import com.programming.orderservice.models.Order;
import com.programming.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderRequest productRequest)  {
        orderService.create(productRequest);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Order> update(@PathVariable("id") UUID id,
                                          @RequestBody OrderUpdateRequest orderUpdateRequest){
        return ResponseEntity.ok(orderService.update(id, orderUpdateRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Order> delete(@PathVariable("id") UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
