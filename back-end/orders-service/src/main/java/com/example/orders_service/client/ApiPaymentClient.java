package com.example.orders_service.client;

import com.example.orders_service.dto.request.PaymentsRequest;
import com.example.orders_service.dto.request.TransactionHistoryRequest;
import com.example.orders_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface ApiPaymentClient {

    @PostMapping("/payments/save")
    ApiResponse<PaymentsRequest> savePayment(@RequestBody PaymentsRequest paymentsRequest);

    @PostMapping("/transactionhistory/save")
    ApiResponse<TransactionHistoryRequest> saveHistory(@RequestBody TransactionHistoryRequest transactionHistoryRequest);

    @PostMapping("/payments/processPayment")
     String processPayment(@RequestParam("amount") long orderTotal,
                              @RequestParam("orderInfo") String orderInfo );
}
