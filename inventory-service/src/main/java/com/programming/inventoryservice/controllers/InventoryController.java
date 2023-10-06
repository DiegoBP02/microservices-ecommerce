package com.programming.inventoryservice.controllers;

import com.programming.inventoryservice.dtos.InventoryUpdateRequest;
import com.programming.inventoryservice.models.Inventory;
import com.programming.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }

    @GetMapping(path = "/isInStock")
    public ResponseEntity<Boolean> isInStock(@RequestParam("productId") UUID productId, // fix queryparam vs requestparam
                                             @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(inventoryService.isInStock(productId,quantity));
    }

    @GetMapping(path = "/stockQuantity/{productId}")
    public ResponseEntity<Integer> stockQuantity(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok(inventoryService.stockQuantity(productId));
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
