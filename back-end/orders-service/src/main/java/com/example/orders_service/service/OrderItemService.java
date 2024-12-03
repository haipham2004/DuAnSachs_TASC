package com.example.orders_service.service;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemService {

    List<OrdersItemsResponse> findOrderItemsByOrderIds(@Param("orderIds") List<Integer> orderIds);
}
