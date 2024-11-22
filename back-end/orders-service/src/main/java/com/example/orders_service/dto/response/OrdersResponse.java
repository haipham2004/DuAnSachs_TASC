package com.example.orders_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
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

    private String status;

    private String shippingAddress;

    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Boolean deletedAt;

    private int userId;

    private String fullNameUsers;

    private String phoneUsers;

    private String emailUser;

    private List<OrdersItemsResponse> ordersItems;

}
