package com.example.orders_service.service.Impl;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.repository.OrderItemRepository;
import com.example.orders_service.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrdersItemsResponse> findOrderItemsByOrderIds(List<Integer> orderIds) {
        return orderItemRepository.findOrderItemsByOrderIds(orderIds);
    }
}
