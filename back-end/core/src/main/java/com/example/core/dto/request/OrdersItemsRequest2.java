package com.example.core.dto.request;

import com.example.core.dto.response.OrderStatus2;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersItemsRequest2 {

    private int orderItemId;

    private Integer orderId;

    private Integer bookId;

    private int quantity;

    private Double price;

    private OrderStatus2 status;

}
