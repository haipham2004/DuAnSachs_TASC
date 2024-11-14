package com.example.orders_service.service.Impl;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.repository.OrdersItemsRepositoryService;
import com.example.orders_service.service.OrdersItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersItemsServiceImpl implements OrdersItemsService {

    private OrdersItemsRepositoryService ordersItemsRepositoryService;

    @Autowired
    public OrdersItemsServiceImpl(OrdersItemsRepositoryService ordersItemsRepositoryService) {
        this.ordersItemsRepositoryService = ordersItemsRepositoryService;
    }

    @Override
    public List<OrdersItemsResponse> findAll() {
        return ordersItemsRepositoryService.findAll();
    }
}
