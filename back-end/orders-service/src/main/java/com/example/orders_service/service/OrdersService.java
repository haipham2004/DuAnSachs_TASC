//package com.example.orders_service.service;
//
//import com.example.orders_service.dto.request.OrdersRequest;
//import com.example.orders_service.dto.response.OrdersResponse;
//import com.example.orders_service.dto.response.PageResponse;
//
//public interface OrdersService {
//
//    PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize);
//
//    OrdersResponse findById(Integer id);
//
//    OrdersResponse findByIdOrder(Integer id);
//
//    OrdersRequest save(OrdersRequest ordersRequest);
//
//    OrdersRequest update(Integer id, OrdersRequest ordersRequest);
//
//    void delete(Integer id);
//
//    OrdersRequest createOrder(OrdersRequest ordersRequest);
//
//    void deleteOrder(Integer orderId);
//
//    OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest);
//
//    void updateOrdersStatus(Integer idOrder,String status);
//
//
//}
