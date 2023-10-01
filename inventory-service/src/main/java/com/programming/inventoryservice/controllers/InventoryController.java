package com.programming.inventoryservice.controllers;

import com.programming.inventoryservice.dtos.InventoryRequest;
import com.programming.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody InventoryRequest productRequest) {
        inventoryService.create(productRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Inventory> update(@PathVariable("id") UUID id,
                                          @RequestBody InventoryUpdateRequest productUpdateRequest){
        return ResponseEntity.ok(inventoryService.update(id, productUpdateRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Inventory> delete(@PathVariable("id") UUID id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
