package com.example.orders_service.entity;



import com.example.orders_service.dto.response.OrderStatus;
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

    private OrderStatus status;

    private String shippingAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;


}
