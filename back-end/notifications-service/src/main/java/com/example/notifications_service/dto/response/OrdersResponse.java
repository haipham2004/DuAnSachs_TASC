package com.example.notifications_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersResponse {

    private int orderId;
    private Double total;
    private String trackingNumber;
    private OrderStatus status;
    private String shippingAddress;
    private int userId;
    private String fullNameUsers;
    private String phoneUsers;
    private String emailUser;
    private List<OrdersItemsResponse> orderItems; // Thay vì đơn giản là 1 OrderItem, bạn có List<OrdersItemsResponse>

    // Constructor sửa lại cho phù hợp với các tham số truyền vào từ JPQL
    public OrdersResponse(int orderId, Double total, String trackingNumber, OrderStatus status,
                          String shippingAddress, int userId, String fullNameUsers, String phoneUsers,
                          String emailUser) {
        this.orderId = orderId;
        this.total = total;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.userId = userId;
        this.fullNameUsers = fullNameUsers;
        this.phoneUsers = phoneUsers;
        this.emailUser = emailUser;
    }

    // Constructor cho phép truyền thêm List<OrderItemsResponse>
    public OrdersResponse(int orderId, Double total, String trackingNumber, OrderStatus status,
                          String shippingAddress, int userId, String fullNameUsers, String phoneUsers,
                          String emailUser, List<OrdersItemsResponse> orderItems) {
        this(orderId, total, trackingNumber, status, shippingAddress, userId, fullNameUsers, phoneUsers, emailUser);
        this.orderItems = orderItems;
    }

    public OrdersResponse(int orderId, Double total, String trackingNumber, OrderStatus status,
                          String shippingAddress, int userId, String fullNameUsers, String phoneUsers,
                          String emailUser, Integer orderItemId, Integer quantity, Double price, Integer orderIds, Integer bookId) {
        this.orderId = orderId;
        this.total = total;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.userId = userId;
        this.fullNameUsers = fullNameUsers;
        this.phoneUsers = phoneUsers;
        this.emailUser = emailUser;
        this.orderItems = new ArrayList<>();
        this.orderItems.add(new OrdersItemsResponse(orderItemId, quantity, price, orderIds, bookId)); // thêm một item vào danh sách
    }


    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Boolean deletedAt;
}
