package com.example.orders_service.repository;

import com.example.orders_service.dto.response.OrdersItemsResponse;

import java.util.List;

public interface OrdersItemsRepositoryService {

    List<OrdersItemsResponse> findAll();
}
