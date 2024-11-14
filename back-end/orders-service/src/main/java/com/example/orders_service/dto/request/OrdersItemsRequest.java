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
public class OrdersItemsRequest {

    private int orderItemId;

    private int orderId;

    private int bookId;

    private int quantity;

    private Double price;

    private String status;

}
