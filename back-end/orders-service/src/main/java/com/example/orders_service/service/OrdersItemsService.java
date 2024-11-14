package com.example.orders_service.service;

import com.example.orders_service.dto.response.OrdersItemsResponse;

import java.util.List;

public interface OrdersItemsService {

     List<OrdersItemsResponse> findAll();
}
