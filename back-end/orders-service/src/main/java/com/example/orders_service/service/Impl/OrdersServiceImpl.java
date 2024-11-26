package com.example.orders_service.service.Impl;


import com.example.orders_service.client.ApiBooksClient;
import com.example.orders_service.client.ApiPaymentClient;
import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.request.PaymentsRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.BooksResponse;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.dto.response.PaymentStatus;
import com.example.orders_service.repository.OrdersRepositoryService;
import com.example.orders_service.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    private OrdersRepositoryService ordersRepositoryService;

    private ApiBooksClient apiBooksClient;

    private ApiPaymentClient apiPaymentClient;

    public static String generateOrderCode() {
        return "DHYKA" + UUID.randomUUID().toString();
    }

    @Autowired
    public OrdersServiceImpl( OrdersRepositoryService ordersRepositoryService,
                             ApiBooksClient apiBooksClient, ApiPaymentClient apiPaymentClient) {
        this.ordersRepositoryService = ordersRepositoryService;
        this.apiBooksClient = apiBooksClient;
        this.apiPaymentClient = apiPaymentClient;

    }

    @Override
    public PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize) {
        return ordersRepositoryService.fillAll(fullName,phone,pageNum,pageSize);
    }

    @Override
    public OrdersResponse findById(Integer id) {
        return ordersRepositoryService.findById(id);
    }

    @Override
    public OrdersResponse findByIdOrder(Integer id) {
        return ordersRepositoryService.findByIdOrder(id);
    }

    @Override
    public OrdersRequest save(OrdersRequest ordersRequest) {
        return ordersRepositoryService.save(ordersRequest);
    }

    @Override
    public OrdersRequest update(Integer id, OrdersRequest ordersRequest) {
        return ordersRepositoryService.update(id, ordersRequest);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
        if (ordersRequest.getOrdersItemsRequests() == null || ordersRequest.getOrdersItemsRequests().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm trong đơn hàng không thể trống");
        }

        double total = 0.0;
        List<Integer> bookIds = ordersRequest.getOrdersItemsRequests().stream()
                .map(OrdersItemsRequest::getBookId)
                .collect(Collectors.toList());

        ApiResponse<List<BooksResponse>> availableBooksResponse = apiBooksClient.getAvailableQuantity(bookIds);

        if (availableBooksResponse == null || availableBooksResponse.getData() == null) {
            throw new IllegalArgumentException("Sản phẩm đã hết hàng");
        }

        Map<Integer, BooksResponse> booksMap = availableBooksResponse.getData().stream()
                .collect(Collectors.toMap(BooksResponse::getBookId, book -> book));

        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            BooksResponse book = booksMap.get(item.getBookId());

            if (book == null) {
                throw new IllegalArgumentException("Không tìm thấy dữ liệu cho sách với ID: " + item.getBookId());
            }

            if (item.getQuantity() > book.getQuantity()) {
                throw new IllegalArgumentException("Số lượng yêu cầu vượt quá tồn kho của sách với ID: " + item.getBookId() +
                        ". Số lượng tồn kho: " + book.getQuantity());
            }

            total += item.getQuantity() * item.getPrice();
        }

        if (ordersRequest.getTotal() != total) {
            throw new IllegalArgumentException("Tổng tiền đơn hàng không hợp lệ: Tổng tiền mong đợi là " + total + " nhưng nhận được " + ordersRequest.getTotal());
        }

        ordersRequest.setTotal(total);
        ordersRequest.setTrackingNumber(generateOrderCode());
        ordersRequest.setStatus(OrderStatus.PENDING);

        OrdersRequest createdOrder = ordersRepositoryService.createOrder(ordersRequest);

        String redirectUrl = apiPaymentClient.processPayment((long) total, String.valueOf(createdOrder.getOrderId()));

        PaymentsRequest paymentRequest = new PaymentsRequest();
        paymentRequest.setIdOrder(createdOrder.getOrderId());
        paymentRequest.setAmount(total);
        paymentRequest.setPaymentMethod("VNPAY");
        paymentRequest.setStatus(PaymentStatus.PENDING.name());
        paymentRequest.setPaymentDate(LocalDateTime.now());

        ApiResponse<PaymentsRequest> paymentResponse = apiPaymentClient.savePayment(paymentRequest);

        if (paymentResponse == null || paymentResponse.getData() == null) {
            throw new IllegalStateException("Không thể lưu thông tin thanh toán");
        }
        createdOrder.setPaymentUrl(redirectUrl);
        return createdOrder;
    }


    @Override
    public void deleteOrder(Integer orderId) {
         ordersRepositoryService.deleteOrder(orderId);
    }

    @Override
    public OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest) {
        return ordersRepositoryService.updateOrder(orderId,updatedOrderRequest);
    }

    @Override
    public void updateOrdersStatus(Integer idOrder, String status) {
        ordersRepositoryService.updateOrdersStatus(idOrder,status);
    }




}
