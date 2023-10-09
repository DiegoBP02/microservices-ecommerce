package com.programming.orderservice.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
}
