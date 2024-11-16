package com.example.orders_service.controller;

import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.service.OrdersItemsService;
import com.example.orders_service.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ApiResponse<PageResponse<OrdersResponse>> findAll(@RequestParam(name = "fullname", defaultValue = "") String fullname,
                                                             @RequestParam(name = "phone", defaultValue = "") String phone,
                                                             @RequestParam(defaultValue = "1") int pageNumber,
                                                             @RequestParam(defaultValue = "5") int pageSize){
        return ApiResponse.<PageResponse<OrdersResponse>>builder().statusCode(200).message("Fill all author").data(ordersService.fillAll(fullname, phone, pageNumber, pageSize)).build();
    }



}
