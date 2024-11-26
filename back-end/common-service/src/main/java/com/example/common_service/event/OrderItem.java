package com.example.common_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {
    private int orderItemId;

    private Integer orderId;

    private Integer bookId;

    private int quantity;

    private Double price;

}
