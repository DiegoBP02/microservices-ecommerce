package com.programming.inventoryservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryUpdateRequest {
    @NotNull
    @Max(value = 10000, message = "Quantity must not exceed 10.000")
    private int quantity;
}
