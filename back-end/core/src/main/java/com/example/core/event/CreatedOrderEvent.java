package com.example.core.event;

import com.example.core.dto.request.OrderItem2;
import com.example.core.dto.response.OrderStatus2;
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

    private List<OrderItem2> items;

    private OrderStatus2 orderStatus2;


}
