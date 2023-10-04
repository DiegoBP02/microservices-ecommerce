package com.programming.productservice.dtos;

import com.programming.productservice.enums.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 50, message = "Product name must be at most 50 characters")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(max = 500, message = "Product description must be at most 500 characters")
    private String description;

    @NotNull
    @DecimalMax(value = "100000.00", message = "Price must not exceed 100000.00")
    private BigDecimal price;

    @NotNull(message = "Product category is required")
    private ProductCategory productCategory;

    @Max(value = 10000, message = "Quantity must not exceed 10.000")
    private Integer quantity;
}
