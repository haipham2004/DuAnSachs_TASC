package com.example.core.dto.request;

import com.example.core.dto.response.OrderStatus2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrdersRequest2 {

    private int orderId;

    private int userId;

    private Double total;

    private String trackingNumber;

    private OrderStatus2 status;

    private String shippingAddress;

    private String paymentMethod;

    private List<OrderItem2> ordersItemsRequests;

}
