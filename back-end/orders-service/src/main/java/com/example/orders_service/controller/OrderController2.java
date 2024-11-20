//package com.example.orders_service.controller;
//
//import com.example.core.dto.response.ApiResponse;
//import com.example.orders_service.command.CreateOrderCommand;
//import com.example.orders_service.dto.request.OrdersRequest;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("api/axon")
//public class OrderController2 {
//
//    private CommandGateway commandGateway;
//
//    @Autowired
//    public OrderController2(CommandGateway commandGateway) {
//        this.commandGateway = commandGateway;
//    }
//
//    @PostMapping
//    public ApiResponse<String> createOrder(@RequestBody OrdersRequest ordersRequest2) {
//        try {
//            // Tạo command từ request
//            CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
//                    .userId(ordersRequest2.getUserId())
//                    .totalAmount(ordersRequest2.getTotal())
//                    .trackingNumber(ordersRequest2.getTrackingNumber())
//                    .shippingAddress(ordersRequest2.getShippingAddress())
//                    .paymentMethod(ordersRequest2.getPaymentMethod())
//                    .items(ordersRequest2.getOrdersItemsRequests())
//                    .orderStatus(ordersRequest2.getStatus())
//                    .build();
//
//            // Gửi command qua Axon Command Gateway
//            commandGateway.sendAndWait(createOrderCommand);
//
//            // Trả về response thành công
//            return ApiResponse.<String>builder()
//                    .statusCode(200)
//                    .message("Order created successfully!")
//                    .data("Order ID: " + createOrderCommand.getOrderId())
//                    .build();
//
//        } catch (Exception ex) {
//            // Xử lý lỗi và trả về response
//            return ApiResponse.<String>builder()
//                    .statusCode(500)
//                    .message("Failed to create order: " + ex.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }
//}
