package com.example.orders_service.repository;

import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;

public interface OrdersRepositoryService {

    PageResponse<OrdersResponse> fillAll(String fullName, String phone,int pageNum, int pageSize);



}
