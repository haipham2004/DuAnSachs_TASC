package com.example.orders_service.controller;

import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.service.OrdersService;
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

    @GetMapping("/findById/{id}")
    public ApiResponse<OrdersResponse> findById(@PathVariable("id") Integer id) {
        OrdersResponse book = ordersService.findById(id);
        return ApiResponse.<OrdersResponse>builder()
                .statusCode(200)
                .message("Order found successfully with ID: " + id)
                .data(book)
                .build();
    }

    @GetMapping("/findByIdOrder/{id}")
    public ApiResponse<OrdersResponse> findByIdOrder(@PathVariable("id") Integer id) {
        OrdersResponse book = ordersService.findByIdOrder(id);
        return ApiResponse.<OrdersResponse>builder()
                .statusCode(200)
                .message("Order found successfully with ID: " + id)
                .data(book)
                .build();
    }


    @PostMapping("save")
    public ApiResponse<OrdersRequest> save(@Valid @RequestBody OrdersRequest ordersRequest) {
        return ApiResponse.<OrdersRequest>builder().statusCode(201).message("Create order succes ").data(ordersService.save(ordersRequest)).build();
    }


    @PutMapping("update/{id}")
    public ApiResponse<OrdersRequest> update(@Valid @RequestBody OrdersRequest ordersRequest, @PathVariable("id") Integer id) {
        return ApiResponse.<OrdersRequest>builder().statusCode(200).message("Update ordersucces ").data(ordersService.update( id,ordersRequest)).build();
    }

    @PostMapping("createOrder")
    public ApiResponse<OrdersRequest> createOrder(@RequestBody OrdersRequest orderRequest) {
        return ApiResponse.<OrdersRequest>builder().statusCode(201).message("Create order succes ").data(ordersService.createOrder(orderRequest)).build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<OrdersRequest> delete(@PathVariable("id") Integer id) {
        ordersService.deleteOrder(id);
        return ApiResponse.<OrdersRequest>builder().statusCode(200).message("Delettee order succes ").build();
    }

    @PutMapping("/updateOrders/{id}")
    public ApiResponse<OrdersRequest> updateOrder(@PathVariable("id") Integer id, @RequestBody OrdersRequest ordersRequest) {
        try {
            // Gọi service để cập nhật đơn hàng
            OrdersRequest updatedOrder = ordersService.updateOrder(id, ordersRequest);

            // Trả về phản hồi với thông tin cập nhật
            return ApiResponse.<OrdersRequest>builder()
                    .statusCode(200)
                    .message("Update order success")
                    .data(updatedOrder)
                    .build();
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi
            return ApiResponse.<OrdersRequest>builder()
                    .statusCode(500)
                    .message("Update order failed: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @PutMapping("updateOrdersStatus")
    public ApiResponse<Void> updateOrdersStatus( @RequestParam("idOrder") Integer idOrder,@RequestParam("status") String status) {
        ordersService.updateOrdersStatus( idOrder,status);
        return ApiResponse.<Void>builder().statusCode(200).message("Update status ordersucces ").build();
    }


}
