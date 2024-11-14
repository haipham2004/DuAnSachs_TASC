package com.example.orders_service.repository;

import com.example.orders_service.dto.response.OrdersResponse;

import java.util.List;

public interface OrdersRepositoryService {

    List<OrdersResponse> fillAll();

}
