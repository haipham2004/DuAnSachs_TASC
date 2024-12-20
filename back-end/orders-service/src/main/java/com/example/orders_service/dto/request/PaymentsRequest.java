package com.example.orders_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentsRequest {

    private Integer idOrder;

    private LocalDateTime paymentDate;

    private Double amount;

    private String paymentMethod;

    private String status;


}
