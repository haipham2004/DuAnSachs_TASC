package com.example.orders_service.controller;

import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.service.OrdersItemsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/findById/{id}")
    public ApiResponse<OrdersItemsResponse> findById(@PathVariable("id") Integer id) {
        OrdersItemsResponse ordersItemsResponse = ordersItemsService.findById(id);
        return ApiResponse.<OrdersItemsResponse>builder()
                .statusCode(200)
                .message("Orderitems found successfully with ID: " + id)
                .data(ordersItemsResponse)
                .build();
    }


    @PostMapping("save")
    public ApiResponse<OrdersItemsRequest> save(@Valid @RequestBody OrdersItemsRequest ordersItemsRequest) {
        return ApiResponse.<OrdersItemsRequest>builder().statusCode(201).message("Create orderitems succes ").data(ordersItemsService.save(ordersItemsRequest)).build();
    }


    @PutMapping("update/{id}")
    public ApiResponse<OrdersItemsRequest> update(@Valid @RequestBody OrdersItemsRequest ordersItemsRequest, @PathVariable("id") Integer id) {
        return ApiResponse.<OrdersItemsRequest>builder().statusCode(200).message("Update ordersucces ").data(ordersItemsService.update( id,ordersItemsRequest)).build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Integer id) {
        ordersItemsService.deleteOrderItem(id);
        return ApiResponse.<Void>builder().statusCode(200).message("Delettee orderItem succes ").build();
    }

    @PutMapping("updateOrder")
    public ApiResponse<Void> updateOrderItem(@RequestParam(name="orderItemId",defaultValue = "0") Integer orderItemId,
                                                           @RequestParam(name="newQuantity",defaultValue = "0") Integer newQuantity) {
        ordersItemsService.updateOrderItem( orderItemId,newQuantity);
        return ApiResponse.<Void>builder().statusCode(200).message("Update ordersucces ").build();
    }

}
