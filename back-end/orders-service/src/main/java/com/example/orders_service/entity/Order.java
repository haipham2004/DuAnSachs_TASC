package com.example.orders_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    private int orderId;

    private int userId;

    private Double total;

    private String trackingNumber;

    private String status;

    private String shippingAddress;

    private String paymentMethod;

    private String paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;


}
