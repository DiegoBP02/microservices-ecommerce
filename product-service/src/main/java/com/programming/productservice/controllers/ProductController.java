package com.programming.productservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.programming.productservice.dtos.ProductRequest;
import com.programming.productservice.dtos.ProductUpdateRequest;
import com.programming.productservice.models.Product;
import com.programming.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ProductRequest productRequest) throws JsonProcessingException {
        productService.create(productRequest);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") UUID id,
                                          @RequestBody ProductUpdateRequest productUpdateRequest){
        return ResponseEntity.ok(productService.update(id, productUpdateRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
