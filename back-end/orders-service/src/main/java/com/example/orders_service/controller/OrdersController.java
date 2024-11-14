package com.example.orders_service.controller;

import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.service.OrdersItemsService;
import com.example.orders_service.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrdersController {

    private OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("findAll")
    public ApiResponse<List<OrdersResponse>> findAll() {
        return ApiResponse.<List<OrdersResponse>>builder().statusCode(200).message("findll orders success").data(ordersService.findAll()).build();
    }

}
