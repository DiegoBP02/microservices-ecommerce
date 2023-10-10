package com.programming.paymentservice.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse implements Serializable{
    private BigDecimal totalAmount;
    private List<OrderItem> orderItemList;
}
