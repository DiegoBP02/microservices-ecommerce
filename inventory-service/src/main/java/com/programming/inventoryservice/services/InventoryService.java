package com.programming.inventoryservice.services;

import com.programming.inventoryservice.dtos.InventoryRequest;
import com.programming.inventoryservice.dtos.InventoryUpdateRabbitMQ;
import com.programming.inventoryservice.dtos.InventoryUpdateRequest;
import com.programming.inventoryservice.dtos.OrderItem;
import com.programming.inventoryservice.exceptions.ResourceNotFoundException;
import com.programming.inventoryservice.models.Inventory;
import com.programming.inventoryservice.repositories.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public void create(InventoryRequest inventoryRequest) {
        Inventory product = Inventory.builder()
                .quantity(inventoryRequest.getQuantity())
                .productId(inventoryRequest.getProductId())
                .build();
        inventoryRepository.save(product);
        log.info("A inventory with productId {} and quantity {} was created.",
                inventoryRequest.getProductId(), inventoryRequest.getQuantity());
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findById(UUID id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Inventory update(UUID id, InventoryUpdateRequest inventoryUpdateRequest) {
        try {
            Inventory product = inventoryRepository.getReferenceById(id);
            updateData(product, inventoryUpdateRequest);

            return inventoryRepository.save(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Inventory inventory, InventoryUpdateRequest productUpdateRequest) {
        inventory.setQuantity(productUpdateRequest.getQuantity());
    }

    public void delete(UUID id) {
        inventoryRepository.deleteById(id);
    }

    private Inventory findByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId));
    }

    public boolean isInStock(UUID productId, Integer quantity) {
        Inventory inventory = findByProductId(productId);

        return inventory.getQuantity() >= quantity;
    }

    public Integer stockQuantity(UUID productId) {
        return findByProductId(productId).getQuantity();
    }

    public void updateProductsFromInventory(InventoryUpdateRabbitMQ inventoryUpdateRabbitMQ) {
        for(OrderItem orderItem : inventoryUpdateRabbitMQ.getOrderItemList()){
            Inventory inventory = findByProductId(orderItem.getProductId());
            inventory.setQuantity(inventory.getQuantity() - orderItem.getQuantity());
            inventoryRepository.save(inventory);
            log.info("Updated inventory for Product ID {}: New Quantity: {}",
                    inventory.getProductId(), inventory.getQuantity());
        }
    }
}
