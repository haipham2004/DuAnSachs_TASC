package com.example.orders_service.controller;

import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class TestController {

    private OrderService orderService;

    @Autowired
    public TestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/findAll")
    public ApiResponse<Page<OrdersResponse>> findAllUserDtoWithPageSearch(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "phone", required = false) String phone) {

        // Điều chỉnh pageNumber vì Pageable bắt đầu từ 0
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        // Gọi service để lấy dữ liệu phân trang
        Page<OrdersResponse> pageResponse = orderService.fillAll(pageable, fullName, phone);

        // Trả về ApiResponse với dữ liệu
        return ApiResponse.<Page<OrdersResponse>>builder()
                .data(pageResponse)
                .build();
    }

    @GetMapping("/findById/{id}")
    public ApiResponse<OrdersResponse> findById(@PathVariable("id") Integer id) {
        OrdersResponse book = orderService.findById(id);
        return ApiResponse.<OrdersResponse>builder()
                .statusCode(200)
                .message("Order found successfully with ID: " + id)
                .data(book)
                .build();
    }

    @GetMapping("/findByIdOrder/{id}")
    public ApiResponse<OrdersResponse> findByIdOrder(@PathVariable("id") Integer id) {
        OrdersResponse book = orderService.findByIdOrder(id);
        return ApiResponse.<OrdersResponse>builder()
                .statusCode(200)
                .message("Order found successfully with ID: " + id)
                .data(book)
                .build();
    }

    @PostMapping("createOrder")
    public ApiResponse<OrdersRequest> createOrder(@RequestBody OrdersRequest orderRequest) {
        return ApiResponse.<OrdersRequest>builder().statusCode(201).message("Create order succes ").data(orderService.createOrder(orderRequest)).build();
    }

    @PutMapping("/updateOrdersStatus")
    public ApiResponse<Void> updateOrdersStatus(
            @RequestParam("idOrder") Integer idOrder,
            @RequestParam("status") String status) {

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
        orderService.updateOrdersStatus(idOrder, orderStatus);
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .message("Cập nhật trạng thái đơn hàng thành công")
                .build();
    }

}
