package com.example.orders_service.entity;

import java.time.LocalDateTime;

public class OrdersItems {

    private int orderItemId;

    private int orderId;

    private int bookId;

    private int quantity;

    private Double price;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;

}
