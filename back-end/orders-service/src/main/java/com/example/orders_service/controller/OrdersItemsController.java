package com.example.orders_service.controller;

import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.service.OrdersItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orders-items")
public class OrdersItemsController {

    private OrdersItemsService ordersItemsService;

    @Autowired
    public OrdersItemsController(OrdersItemsService ordersItemsService) {
        this.ordersItemsService = ordersItemsService;
    }

    @GetMapping("findAll")
    public ApiResponse<List<OrdersItemsResponse>> findAll(){
        return ApiResponse.<List<OrdersItemsResponse>>builder().statusCode(200).message("findll orders success").data(ordersItemsService.findAll()).build();
    }


    @GetMapping("getOrderWithItems/{id}")
    public ApiResponse<List<OrdersItemsResponse>> getOrderWithItems(@PathVariable("id") Integer id){
        return ApiResponse.<List<OrdersItemsResponse>>builder().statusCode(200).message("findll orders success").data(ordersItemsService.getOrderWithItems(id)).build();
    }
}
