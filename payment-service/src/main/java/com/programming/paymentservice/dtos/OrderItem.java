package com.programming.paymentservice.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class OrderItem implements Serializable {
    private UUID productId;
    private int quantity;
}
