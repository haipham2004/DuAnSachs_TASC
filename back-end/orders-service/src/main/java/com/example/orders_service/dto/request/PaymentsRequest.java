package com.example.orders_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PaymentsRequest {

    private Integer idOrder;

    private LocalDateTime paymentDate;

    private Double amount;

    private String paymentMethod;

    private String status;
}
