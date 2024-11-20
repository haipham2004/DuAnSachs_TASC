package com.example.orders_service.service;

import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.OrdersItemsResponse;

import java.util.List;

public interface OrdersItemsService {

     List<OrdersItemsResponse> findAll();

     List<OrdersItemsResponse> getOrderWithItems(int orderId);

     OrdersItemsResponse findById(Integer id);

     OrdersItemsRequest save(OrdersItemsRequest ordersItemsRequest);

     OrdersItemsRequest update(Integer id, OrdersItemsRequest ordersItemsRequest);

     void delete(Integer id);

     void deleteOrderItem(Integer orderItemId);

     void updateOrderItem(int orderItemId, int newQuantity);
}
