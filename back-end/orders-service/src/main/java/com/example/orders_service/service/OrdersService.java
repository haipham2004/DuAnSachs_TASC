package com.example.orders_service.service;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;

import java.util.List;

public interface OrdersService {

    PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize);


}
