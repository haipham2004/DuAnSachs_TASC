//package com.example.orders_service.repository.Impl;
//
//
//import com.example.orders_service.client.ApiBooksClient;
//import com.example.orders_service.client.ApiPaymentClient;
//import com.example.orders_service.dto.request.OrdersItemsRequest;
//import com.example.orders_service.dto.request.OrdersRequest;
//import com.example.orders_service.dto.response.OrderItemStatus;
//import com.example.orders_service.dto.response.OrderStatus;
//import com.example.orders_service.dto.response.OrdersItemsResponse;
//import com.example.orders_service.dto.response.OrdersResponse;
//import com.example.orders_service.dto.response.PageResponse;
//import com.example.orders_service.exception.NotfoundException;
//import com.example.orders_service.mapper.OrdersRowMapper;
//import com.example.orders_service.mapper.OrdersRowMapper2;
//import com.example.orders_service.repository.OrdersRepositoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class OdersRepositoryServiceImpl implements OrdersRepositoryService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    private ApiBooksClient apiBooksClient;
//
//
//    @Autowired
//    public OdersRepositoryServiceImpl(JdbcTemplate jdbcTemplate, ApiBooksClient apiBooksClient) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.apiBooksClient = apiBooksClient;
//    }
//
//
//    private OrdersRowMapper getOrdersMapper() {
//        return new OrdersRowMapper();
//    }
//
//    @Override
//    public PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize) {
//
//        int offset = (pageNum - 1) * pageSize;
//
//        String sql = "SELECT " +
//                "o.order_id, o.total, o.tracking_number, o.status, " +
//                "o.shipping_address, " +
//                "o.created_at, o.updated_at, o.deleted_at, " +
//                "o.user_id, u.fullname, u.phone, u.email " +
//                "FROM orders o " +
//                "JOIN users u ON o.user_id = u.user_id " +
//                "WHERE (u.fullname LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
//                "AND (u.phone LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
//                "LIMIT ? OFFSET ?";
//
//
//        List<OrdersResponse> orders = jdbcTemplate.query(
//                sql,
//                getOrdersMapper(),
//                fullName, fullName, phone, phone, pageSize, offset
//        );
//
//        String countSql = "SELECT COUNT(*) FROM orders o " +
//                "JOIN users u ON o.user_id = u.user_id " +
//                "WHERE (u.fullname LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
//                "AND (u.phone LIKE CONCAT('%', ?, '%') OR ? IS NULL)";
//
//
//        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, fullName, fullName, phone, phone);
//
//
//        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
//
//        return new PageResponse<>(orders, totalElements, totalPages, pageNum);
//    }
//
//    @Override
//    public OrdersResponse findById(Integer id) {
//        String sql = "SELECT " +
//                "o.order_id, o.total, o.tracking_number, o.status, " +
//                "o.shipping_address, " +
//                "o.created_at, o.updated_at, o.deleted_at, " +
//                "o.user_id, u.fullname, u.phone, u.email " +
//                "FROM orders o " +
//                "JOIN users u ON o.user_id = u.user_id " +
//                "WHERE o.order_id = ?";
//
//        try {
//            return jdbcTemplate.queryForObject(sql, getOrdersMapper(), id);
//        } catch (EmptyResultDataAccessException e) {
//            throw new NotfoundException("Order with ID " + id + " not found.");
//        }
//    }
//
//    @Override
//    public OrdersResponse findByIdOrder(Integer id) {
//        String sql = "SELECT o.order_id, o.total, o.tracking_number, o.status, " +
//                "o.shipping_address, " +
//                "o.created_at, o.updated_at, o.deleted_at, " +
//                "o.user_id, u.fullname, u.phone, u.email, " +
//                "oi.order_item_id, oi.quantity, oi.price, b.title, oi.book_id " +
//                "FROM orders o " +
//                "JOIN users u ON o.user_id = u.user_id " +
//                "JOIN order_items oi ON oi.order_id = o.order_id " +
//                "LEFT JOIN books b ON b.book_id = oi.book_id " +
//                "WHERE o.order_id = ?";
//
//
//        try {
//            return jdbcTemplate.queryForObject(sql, new OrdersRowMapper2(), id);
//        } catch (EmptyResultDataAccessException e) {
//            throw new NotfoundException("Order with IDs " + id + " not found.");
//        }
//    }
//
//    @Override
//    public OrdersRequest save(OrdersRequest ordersRequest) {
//        String satus = OrderStatus.CREATED.name();
//        String trackingNumber = UUID.randomUUID().toString();
//        String sql = "INSERT INTO orders (total, tracking_number, status, shipping_address, " +
//                "user_id) " +
//                "VALUES (?, ?, ?, ?, ?)";
//
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setDouble(1, ordersRequest.getTotal());
//            ps.setString(2, trackingNumber);
//            ps.setString(3, satus);
//            ps.setString(4, ordersRequest.getShippingAddress());
//            ps.setInt(5, ordersRequest.getUserId());
//            return ps;
//        }, keyHolder);
//
//        ordersRequest.setOrderId(keyHolder.getKey().intValue());
//
//        return ordersRequest;
//    }
//
//    @Override
//    public OrdersRequest update(Integer id, OrdersRequest ordersRequest) {
//        String sql = "UPDATE orders SET total = ?, tracking_number = ?, status = ?, " +
//                "shipping_address = ? " +
//                "WHERE order_id = ?";
//
//        jdbcTemplate.update(sql,
//                ordersRequest.getTotal(),
//                ordersRequest.getTrackingNumber(),
//                ordersRequest.getStatus(),
//                ordersRequest.getShippingAddress(),
//                id);
//
//        ordersRequest.setOrderId(id);
//        return ordersRequest;
//    }
//
//    @Override
//    public void delete(Integer id) {
//        String sql = "UPDATE orders SET deleted_at = true WHERE order_id = ?";
//        jdbcTemplate.update(sql, id);
//    }
//
//    @Override
//    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
//        String sqlOrder = "INSERT INTO orders (total, tracking_number, status, shipping_address, user_id) " +
//                "VALUES (?, ?, ?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
//            ps.setDouble(1, ordersRequest.getTotal());
//            ps.setString(2, ordersRequest.getTrackingNumber());
//            ps.setString(3, OrderStatus.PENDING.name());
//            ps.setString(4, ordersRequest.getShippingAddress());
//            ps.setInt(5, ordersRequest.getUserId());
//            return ps;
//        }, keyHolder);
//
//        int orderId = keyHolder.getKey().intValue();
//        ordersRequest.setOrderId(orderId);
//        ordersRequest.setStatus(OrderStatus.CREATED);
//
//        // Lưu chi tiết các mặt hàng trong đơn hàng
//        String sqlOrderItem = "INSERT INTO order_items (order_id, book_id, quantity, price, status) " +
//                "VALUES (?, ?, ?, ?, ?)";
//        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
//            KeyHolder itemKeyHolder = new GeneratedKeyHolder();
//            jdbcTemplate.update(connection -> {
//                PreparedStatement ps = connection.prepareStatement(sqlOrderItem, Statement.RETURN_GENERATED_KEYS);
//                ps.setInt(1, orderId);
//                ps.setInt(2, item.getBookId());
//                ps.setInt(3, item.getQuantity());
//                ps.setDouble(4, item.getPrice());
//                ps.setString(5, OrderItemStatus.PENDING.name());
//                return ps;
//            }, itemKeyHolder);
//
//            int orderItemId = itemKeyHolder.getKey().intValue();
//            item.setOrderItemId(orderItemId);
//            item.setStatus(OrderItemStatus.PENDING);
//        }
//        return ordersRequest;
//    }
//
//
//    @Override
//    public OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest) {
//        return null;
//    }
//
//
//    @Override
//    public void deleteOrder(Integer orderId) {
//        // Bước 1: Lấy danh sách order items liên quan đến order
//        String sqlGetOrderItems = "SELECT book_id, quantity FROM order_items WHERE order_id = ?";
//        List<OrdersItemsResponse> orderItems = jdbcTemplate.query(sqlGetOrderItems, new RowMapper<OrdersItemsResponse>() {
//            @Override
//            public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
//                OrdersItemsResponse ordersItemsResponse = new OrdersItemsResponse();
//                ordersItemsResponse.setBookId(rs.getInt("book_id"));
//                ordersItemsResponse.setQuantity(rs.getInt("quantity"));
//                return ordersItemsResponse;
//            }
//        }, orderId);
//
//        // Kiểm tra nếu không tìm thấy order items
//        if (orderItems.isEmpty()) {
//            throw new RuntimeException("No order items found for order ID: " + orderId);
//        }
//
//        // Bước 2: Cập nhật số lượng sách trong kho
//        for (OrdersItemsResponse item : orderItems) {
//            try {
//                apiBooksClient.increaseQuantity(item.getBookId(), item.getQuantity()); // Sử dụng số lượng dương để tăng lại
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to update quantity for book ID: " + item.getBookId(), e);
//            }
//        }
//
//        String sqlDeleteOrderItems = "DELETE FROM order_items WHERE order_id = ?";
//        int rowsAffected = jdbcTemplate.update(sqlDeleteOrderItems, orderId);
//
//
//        if (rowsAffected == 0) {
//            throw new RuntimeException("Failed to delete order items for order ID: " + orderId);
//        }
//
//
//        String sqlDeleteOrder = "DELETE FROM orders WHERE order_id = ?";
//        rowsAffected = jdbcTemplate.update(sqlDeleteOrder, orderId);
//
//        if (rowsAffected == 0) {
//            throw new RuntimeException("Failed to delete order with ID: " + orderId);
//        }
//    }
//
//    @Override
//    public void updateOrdersStatus(Integer idOrder, String status) {
//        String sqlUpdateOrderStatus = "UPDATE orders SET status = ? WHERE order_id = ?";
//        jdbcTemplate.update(sqlUpdateOrderStatus, status, idOrder);
//    }
//}