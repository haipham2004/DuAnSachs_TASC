package com.example.orders_service.service.Impl;

import com.example.orders_service.client.ApiBooksClient;
import com.example.orders_service.client.ApiPaymentClient;
import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.BooksResponse;
import com.example.orders_service.dto.response.OrderItemStatus;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.entity.OrderItems;
import com.example.orders_service.repository.OrderItemRepository;
import com.example.orders_service.repository.OrdersRepository;
import com.example.orders_service.service.OrderItemService;
import com.example.orders_service.service.OrderService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    public static String generateOrderCode() {
        return "DHYKA" + UUID.randomUUID().toString();
    }

    private OrdersRepository ordersRepository;

    private OrderItemRepository orderItemRepository;
    private ApiBooksClient apiBooksClient;
    private ApiPaymentClient apiPaymentClient;
    private ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository, OrderItemRepository orderItemRepository,
                            ApiBooksClient apiBooksClient, ApiPaymentClient apiPaymentClient, ModelMapper modelMapper) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.apiBooksClient = apiBooksClient;
        this.apiPaymentClient = apiPaymentClient;
        this.modelMapper = modelMapper;
    }


    @Override
    public Page<OrdersResponse> fillAll(Pageable pageable, String fullName, String phone) {

        Page<OrdersResponse> orders = ordersRepository.findAllOrdersWithSearch(pageable, fullName, phone);

        // Lấy danh sách orderId từ các đơn hàng
        List<Integer> orderIds = orders.getContent().stream()
                .map(OrdersResponse::getOrderId)
                .collect(Collectors.toList());

        log.info("Danh sách id order: "+orderIds);
        // Lấy tất cả OrderItems theo orderIds
        List<OrdersItemsResponse> orderItems = orderItemRepository.findOrderItemsByOrderIds(orderIds);

        log.info("Danh sách order_item: "+orderItems.toString());

        // Nhóm các OrderItems theo orderId
        Map<Integer, List<OrdersItemsResponse>> orderItemsMap = orderItems.stream()
                .collect(Collectors.groupingBy(item -> item.getOrderId())); // Nhóm theo orderId

        // Cập nhật các OrderItems vào mỗi OrdersResponse
        orders.getContent().forEach(order -> {
            List<OrdersItemsResponse> items = orderItemsMap.get(order.getOrderId());
            if (items != null) {
                order.setOrderItems(items);  // Gán danh sách OrderItems vào Order
            } else {
                order.setOrderItems(Collections.emptyList()); // Nếu không có OrderItems, gán danh sách rỗng
            }
        });

        return orders;

    }

    @Override
    public OrdersResponse findById(Integer id) {
        // Use the repository to fetch an order by ID
        return ordersRepository.findById(id)
                .map(order -> modelMapper.map(order, OrdersResponse.class))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrdersResponse findByIdOrder(Integer id) {
        OrdersResponse orderResponse = ordersRepository.findOrderById(id);

        // Lấy danh sách order items của order đó
        List<OrdersItemsResponse> orderItems = orderItemRepository.findOrderItemsByOrderId(id);

        // Set danh sách order items vào trong order response
        if (orderResponse != null) {
            orderResponse.setOrderItems(orderItems);
        }

        return orderResponse;
    }

    @Override
    public OrdersRequest save(OrdersRequest ordersRequest) {
        // Convert OrdersRequest to Order entity and save
        Order order = modelMapper.map(ordersRequest, Order.class);
        order = ordersRepository.save(order);
        return modelMapper.map(order, OrdersRequest.class);
    }

    @Override
    public OrdersRequest update(Integer id, OrdersRequest ordersRequest) {
        // Retrieve the existing order, update it, and save
        Order order = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        modelMapper.map(ordersRequest, order);
        order = ordersRepository.save(order);
        return modelMapper.map(order, OrdersRequest.class);
    }

    @Override
    public void delete(Integer id) {
        // Delete the order using Spring Data JPA delete method
        ordersRepository.deleteById(id);
    }

    @Override
    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
        if (ordersRequest.getOrdersItemsRequests() == null || ordersRequest.getOrdersItemsRequests().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        double total = 0.0;
        List<Integer> bookIds = ordersRequest.getOrdersItemsRequests().stream()
                .map(OrdersItemsRequest::getBookId)
                .collect(Collectors.toList());

        ApiResponse<List<BooksResponse>> availableBooksResponse = apiBooksClient.getAvailableQuantity(bookIds);

        if (availableBooksResponse == null || availableBooksResponse.getData() == null) {
            throw new IllegalArgumentException("Out of stock");
        }

        Map<Integer, BooksResponse> booksMap = availableBooksResponse.getData().stream()
                .collect(Collectors.toMap(BooksResponse::getBookId, book -> book));

        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            BooksResponse book = booksMap.get(item.getBookId());
            if (book == null) {
                throw new IllegalArgumentException("Book not found with ID: " + item.getBookId());
            }
            if (item.getQuantity() > book.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book ID: " + item.getBookId());
            }
            total += item.getQuantity() * item.getPrice();
        }

        if (ordersRequest.getTotal() != total) {
            throw new IllegalArgumentException("Order total mismatch");
        }

        ordersRequest.setTotal(total);
        ordersRequest.setTrackingNumber(generateOrderCode());
        ordersRequest.setStatus(OrderStatus.PENDING);

        // Save order and order items
        Order order = modelMapper.map(ordersRequest, Order.class);
        order = ordersRepository.save(order);

        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setBookId(item.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setStatus(OrderItemStatus.PENDING);
            orderItem.setOrderId(order.getOrderId());

            // Lưu OrderItem vào cơ sở dữ liệu
            orderItemRepository.save(orderItem);
        }

        // Create payment request and save it
        String redirectUrl = apiPaymentClient.processPayment((long) total, String.valueOf(order.getOrderId()));
        ordersRequest.setPaymentUrl(redirectUrl);
        return ordersRequest;
    }

    @Override
    public void deleteOrder(Integer orderId) {
        ordersRepository.deleteById(orderId);
    }

    @Override
    public OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest) {
        return update(orderId, updatedOrderRequest);
    }

    @Override
    public void updateOrdersStatus(Integer idOrder, OrderStatus status) {
        Order order = ordersRepository.findById(idOrder)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));

        order.setStatus(status);

        ordersRepository.save(order);
    }


}
