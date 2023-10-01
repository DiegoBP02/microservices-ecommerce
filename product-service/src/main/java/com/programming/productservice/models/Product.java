package com.programming.productservice.models;

import com.programming.productservice.enums.ProductCategory;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "t_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private ProductCategory category;
}
