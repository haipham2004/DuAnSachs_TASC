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
import java.util.UUID;
import java.util.List;
import java.util.Map;
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
    public OrdersServiceImpl(OrdersRepositoryService ordersRepositoryService, ApiBooksClient apiBooksClient, ApiPaymentClient apiPaymentClient) {
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
        // Kiểm tra danh sách sản phẩm trong đơn hàng không thể trống
        if (ordersRequest.getOrdersItemsRequests() == null || ordersRequest.getOrdersItemsRequests().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm trong đơn hàng không thể trống");
        }

        // Tính tổng tiền đơn hàng và kiểm tra thông tin sách
        double total = 0.0;
        List<Integer> bookIds = ordersRequest.getOrdersItemsRequests().stream()
                .map(OrdersItemsRequest::getBookId)
                .collect(Collectors.toList());

        // Lấy thông tin số lượng và giá của các cuốn sách từ API
        ApiResponse<List<BooksResponse>> availableBooksResponse = apiBooksClient.getAvailableQuantity(bookIds);

        if (availableBooksResponse == null || availableBooksResponse.getData() == null) {
            throw new IllegalArgumentException("Không thể lấy thông tin sách có sẵn");
        }

        // Chuyển đổi danh sách sách thành bản đồ để tra cứu nhanh theo bookId
        Map<Integer, BooksResponse> booksMap = availableBooksResponse.getData().stream()
                .collect(Collectors.toMap(BooksResponse::getBookId, book -> book));

        // Duyệt qua từng item để tính tổng tiền và kiểm tra số lượng tồn kho
        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            BooksResponse book = booksMap.get(item.getBookId());

            if (book == null) {
                throw new IllegalArgumentException("Không tìm thấy dữ liệu cho sách với ID: " + item.getBookId());
            }

            // Kiểm tra số lượng tồn kho
            if (item.getQuantity() > book.getQuantity()) {
                throw new IllegalArgumentException("Số lượng yêu cầu vượt quá tồn kho của sách với ID: " + item.getBookId() +
                        ". Số lượng tồn kho: " + book.getQuantity());
            }

            // Tính tổng tiền cho mỗi item và cộng vào tổng tiền đơn hàng
            // Dùng giá của orderItem (trong ordersItemsRequest) thay vì lấy giá của book
            total += item.getQuantity() * item.getPrice(); // Sử dụng item.getPrice() thay vì book.getPrice()
        }

        // Kiểm tra lại tổng tiền
        if (ordersRequest.getTotal() != total) {
            throw new IllegalArgumentException("Tổng tiền đơn hàng không hợp lệ: Tổng tiền mong đợi là " + total + " nhưng nhận được " + ordersRequest.getTotal());
        }

        // Lưu thông tin đơn hàng vào cơ sở dữ liệu
        ordersRequest.setTotal(total);
        ordersRequest.setTrackingNumber(generateOrderCode());
        ordersRequest.setStatus(OrderStatus.CREATED);

        OrdersRequest createdOrder = ordersRepositoryService.createOrder(ordersRequest);

        // Tạo URL thanh toán
        String redirectUrl = apiPaymentClient.processPayment((long) total, String.valueOf(createdOrder.getOrderId()));

        // Lưu thông tin thanh toán
        PaymentsRequest paymentRequest = new PaymentsRequest();
        paymentRequest.setIdOrder(createdOrder.getOrderId());
        paymentRequest.setAmount(total);
        paymentRequest.setPaymentMethod("VNPAY");
        paymentRequest.setStatus(PaymentStatus.PENDING.name());
        paymentRequest.setRedirectUrl(redirectUrl);

        ApiResponse<PaymentsRequest> paymentResponse = apiPaymentClient.savePayment(paymentRequest);

        if (paymentResponse == null || paymentResponse.getData() == null) {
            throw new IllegalStateException("Không thể lưu thông tin thanh toán");
        }

        // Cập nhật URL thanh toán vào đơn hàng
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
