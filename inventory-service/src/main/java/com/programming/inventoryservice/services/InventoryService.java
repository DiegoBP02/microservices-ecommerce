package com.programming.inventoryservice.services;

import com.programming.inventoryservice.dtos.InventoryRequest;
import com.programming.inventoryservice.dtos.InventoryUpdateRequest;
import com.programming.inventoryservice.exceptions.ResourceNotFoundException;
import com.programming.inventoryservice.models.Inventory;
import com.programming.inventoryservice.repositories.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public void create(InventoryRequest inventoryRequest) {
        Inventory product = Inventory.builder()
                .quantity(inventoryRequest.getQuantity())
                .build();
        inventoryRepository.save(product);
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

}
