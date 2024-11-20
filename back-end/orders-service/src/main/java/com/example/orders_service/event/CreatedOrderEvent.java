package com.example.orders_service.event;

import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.OrderStatus;
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
public class CreatedOrderEvent {

    private Integer orderId;

    private Integer userId;

    private Double totalAmount;

    private String trackingNumber;

    private String shippingAddress;

    private String paymentMethod;

    private List<OrdersItemsRequest> items;

    private OrderStatus orderStatus;


}
