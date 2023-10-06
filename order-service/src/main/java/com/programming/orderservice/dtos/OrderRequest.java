package com.programming.orderservice.dtos;

import com.programming.orderservice.models.OrderItem;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotEmpty
    private List<OrderItem> orderItemList;
}
