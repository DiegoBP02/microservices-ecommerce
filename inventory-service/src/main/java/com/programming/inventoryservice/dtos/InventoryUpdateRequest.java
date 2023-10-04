package com.programming.inventoryservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryUpdateRequest {
    @NotNull
    @Max(value = 10000, message = "Quantity must not exceed 10.000")
    private int quantity;
}
