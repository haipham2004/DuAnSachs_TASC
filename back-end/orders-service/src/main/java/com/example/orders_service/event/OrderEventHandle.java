package com.example.orders_service.event;

import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.service.OrdersService;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandle {

    private OrdersService ordersService;

    @Autowired
    public OrderEventHandle(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @EventHandler
    public void on(CreatedOrderEvent event) {

        OrdersRequest ordersRequest = OrdersRequest.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId()) // Giả sử bạn có các thông tin khác trong sự kiện
                .total(event.getTotalAmount())
                .trackingNumber(null)
                .status(event.getOrderStatus())
                .shippingAddress(event.getShippingAddress())
                .paymentMethod(event.getPaymentMethod())
                .ordersItemsRequests(event.getItems())
                .shippingAddress(event.getShippingAddress())
                .build();

        // Gọi service để xử lý đơn hàng (ví dụ: lưu vào cơ sở dữ liệu)
        ordersService.createOrder(ordersRequest);
    }
}
