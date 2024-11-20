package com.example.orders_service.service.Impl;

import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
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
    public PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize) {
        return ordersRepositoryService.fillAll(fullName,phone,pageNum,pageSize);
    }

    @Override
    public OrdersResponse findById(Integer id) {
        return ordersRepositoryService.findById(id);
    }

    @Override
    public OrdersResponse findByIdOrder(Integer id) {
        return ordersRepositoryService.findByIdOrder(id);
    }

    @Override
    public OrdersRequest save(OrdersRequest ordersRequest) {
        return ordersRepositoryService.save(ordersRequest);
    }

    @Override
    public OrdersRequest update(Integer id, OrdersRequest ordersRequest) {
        return ordersRepositoryService.update(id, ordersRequest);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
        return ordersRepositoryService.createOrder(ordersRequest);
    }

    @Override
    public void deleteOrder(Integer orderId) {
         ordersRepositoryService.deleteOrder(orderId);
    }

    @Override
    public OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest) {
        return ordersRepositoryService.updateOrder(orderId,updatedOrderRequest);
    }

    @Override
    public void updateOrdersStatus(Integer idOrder, String status) {
        ordersRepositoryService.updateOrdersStatus(idOrder,status);
    }


}
