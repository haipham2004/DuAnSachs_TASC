package com.example.orders_service.service.Impl;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.repository.OrdersRepositoryService;
import com.example.orders_service.service.OrdersItemsService;
import com.example.orders_service.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    private OrdersRepositoryService ordersRepositoryService;

    @Autowired
    public OrdersServiceImpl(OrdersRepositoryService ordersRepositoryService) {
        this.ordersRepositoryService = ordersRepositoryService;
    }


    @Override
    public List<OrdersResponse> findAll() {
        return ordersRepositoryService.fillAll();
    }
}
