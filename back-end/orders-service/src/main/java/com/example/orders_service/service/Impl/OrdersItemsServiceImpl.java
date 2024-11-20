package com.example.orders_service.service.Impl;

import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.exception.NotfoundException;
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

    @Override
    public List<OrdersItemsResponse> getOrderWithItems(int orderId) {
        return ordersItemsRepositoryService.getOrderWithItems(orderId);
    }

    @Override
    public OrdersItemsResponse findById(Integer id) {
        return ordersItemsRepositoryService.findById(id);
    }

    @Override
    public OrdersItemsRequest save(OrdersItemsRequest ordersItemsRequest) {
        return ordersItemsRepositoryService.save(ordersItemsRequest);
    }

    @Override
    public OrdersItemsRequest update(Integer id, OrdersItemsRequest ordersItemsRequest) {
        return ordersItemsRepositoryService.update(id, ordersItemsRequest);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deleteOrderItem(Integer orderItemId) {
        OrdersItemsResponse ordersItemsResponse=ordersItemsRepositoryService.findById(orderItemId);
        if(ordersItemsResponse==null){
            throw new NotfoundException("Không tồn tại Id orderitem: "+orderItemId);
        }
        ordersItemsRepositoryService.deleteOrderItem(orderItemId);
    }

    @Override
    public void updateOrderItem(int orderItemId, int newQuantity) {
        ordersItemsRepositoryService.updateOrderItem(orderItemId,newQuantity);
    }
}
