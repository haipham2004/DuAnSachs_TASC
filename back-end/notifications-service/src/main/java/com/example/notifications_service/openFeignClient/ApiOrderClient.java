package com.example.notifications_service.openFeignClient;

import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.OrdersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ORDERS-SERVICE")
public interface ApiOrderClient {

    @GetMapping("/orders/findByIdOrder/{id}")
    ApiResponse<OrdersResponse> findByIdOrder(@PathVariable("id") Integer id);
}
