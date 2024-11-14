package com.example.orders_service.service;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;

import java.util.List;

public interface OrdersService {

    List<OrdersResponse> findAll();
}
