package com.example.orders_service.client;

import com.example.orders_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("BOOKS-SERVICE")
public interface ApiBooksClient {

    @GetMapping("/books/getAvailableQuantity")
    public ApiResponse<Integer> getAvailableQuantity(@RequestParam("bookId") Integer bookId);

    @PutMapping("/books/decreaseQuantity")
    public ApiResponse<Void> decreaseQuantity(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                              @RequestParam(name="quantity",defaultValue = "0") Integer quantity);


    @PutMapping("/books/increaseQuantity")
    public ApiResponse<Void> increaseQuantity(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                              @RequestParam(name="quantity",defaultValue = "0") Integer quantity);


}
