package com.example.orders_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrdersRequest {

    private int orderId;

    private int userId;

    private Double total;

    private String trackingNumber;

    private String status;

    private String shippingAddress;

    private String paymentMethod;

    private String paymentStatus;

}
