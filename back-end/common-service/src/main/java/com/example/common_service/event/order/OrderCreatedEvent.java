package com.example.common_service.event.order;

import com.example.common_service.event.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreatedEvent {

    private Integer orderId;

    private Integer userId;

    private Double total;

    private String trackingNumber;

    private String status;

    private String shippingAddress;

    private List<OrderItem> orderItems;
}
