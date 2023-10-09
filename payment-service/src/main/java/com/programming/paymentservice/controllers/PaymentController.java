package com.programming.paymentservice.controllers;

import com.programming.paymentservice.dtos.PaymentRequest;
import com.programming.paymentservice.models.Payment;
import com.programming.paymentservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PaymentRequest productRequest)  {
        paymentService.create(productRequest);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Payment> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Payment> delete(@PathVariable("id") UUID id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
