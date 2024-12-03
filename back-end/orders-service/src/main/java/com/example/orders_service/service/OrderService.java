package com.example.orders_service.service;

import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.dto.response.OrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrdersResponse> fillAll(Pageable pageable, String fullName, String phone);

    OrdersResponse findById(Integer id);

    OrdersResponse findByIdOrder(Integer id);

    OrdersRequest save(OrdersRequest ordersRequest);

    OrdersRequest update(Integer id, OrdersRequest ordersRequest);

    void delete(Integer id);

    OrdersRequest createOrder(OrdersRequest ordersRequest);

    void deleteOrder(Integer orderId);

    OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest);

    void updateOrdersStatus(Integer idOrder, OrderStatus status);
}
