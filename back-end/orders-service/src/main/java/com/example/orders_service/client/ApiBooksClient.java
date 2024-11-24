package com.example.orders_service.client;

import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.BooksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("BOOKS-SERVICE")
public interface ApiBooksClient {


    @GetMapping("/books/getAvailableQuantity")
    ApiResponse<List<BooksResponse>> getAvailableQuantity(@RequestParam("bookId") List<Integer> bookId);

    @PutMapping("/books/decreaseQuantity")
     ApiResponse<Void> decreaseQuantity(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                              @RequestParam(name="quantity",defaultValue = "0") Integer quantity);


    @PutMapping("/books/increaseQuantity")
     ApiResponse<Void> increaseQuantity(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                              @RequestParam(name="quantity",defaultValue = "0") Integer quantity);


}
