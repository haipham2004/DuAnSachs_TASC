package com.example.orders_service.repository;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;

import java.util.List;

public interface OrdersItemsRepositoryService {

    List<OrdersItemsResponse> findAll();

     List<OrdersItemsResponse> getOrderWithItems(int orderId);
}
