package com.example.core.dto.request;

import com.example.core.dto.response.OrderItemStatus2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem2 {

    private Integer bookId;        // Định danh sản phẩm
    private int quantity;          // Số lượng sản phẩm
    private double price;          // Giá mỗi sản phẩm
    private OrderItemStatus2 status; // Trạng thái của mục hàng

}