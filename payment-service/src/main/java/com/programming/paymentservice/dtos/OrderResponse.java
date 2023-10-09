package com.programming.paymentservice.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponse {
    private BigDecimal totalAmount;
}
