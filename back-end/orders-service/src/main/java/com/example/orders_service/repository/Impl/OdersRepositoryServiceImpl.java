package com.example.orders_service.repository.Impl;

import com.example.orders_service.client.ApiBooksClient;
import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.request.OrdersRequest;
import com.example.orders_service.dto.response.ApiResponse;
import com.example.orders_service.dto.response.OrderItemStatus;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.dto.response.PaymentStatus;
import com.example.orders_service.exception.NotfoundException;
import com.example.orders_service.mapper.OrdersRowMapper;
import com.example.orders_service.mapper.OrdersRowMapper2;
import com.example.orders_service.repository.OrdersRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OdersRepositoryServiceImpl implements OrdersRepositoryService {

    private final JdbcTemplate jdbcTemplate;

    private ApiBooksClient apiBooksClient;

    @Autowired
    public OdersRepositoryServiceImpl(JdbcTemplate jdbcTemplate, ApiBooksClient apiBooksClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.apiBooksClient = apiBooksClient;
    }

    public static String generateOrderCode() {
        return "DHYKA" + UUID.randomUUID().toString();
    }

    private OrdersRowMapper getOrdersMapper() {
        return new OrdersRowMapper();
    }

    @Override
    public PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;

        String sql = "SELECT " +
                "o.order_id, o.total, o.tracking_number, o.status, " +
                "o.shipping_address, o.payment_method, " +
                "o.created_at, o.updated_at, o.deleted_at, " +
                "o.user_id, u.fullname, u.phone, u.email " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE (u.fullname LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "AND (u.phone LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "LIMIT ? OFFSET ?";


        List<OrdersResponse> orders = jdbcTemplate.query(
                sql,
                getOrdersMapper(),
                fullName, fullName, phone, phone, pageSize, offset
        );

        String countSql = "SELECT COUNT(*) FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE (u.fullname LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "AND (u.phone LIKE CONCAT('%', ?, '%') OR ? IS NULL)";


        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, fullName, fullName, phone, phone);


        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(orders, totalElements, totalPages, pageNum);
    }

    @Override
    public OrdersResponse findById(Integer id) {
        String sql = "SELECT " +
                "o.order_id, o.total, o.tracking_number, o.status, " +
                "o.shipping_address, o.payment_method," +
                "o.created_at, o.updated_at, o.deleted_at, " +
                "o.user_id, u.fullname, u.phone, u.email " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.order_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, getOrdersMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotfoundException("Order with ID " + id + " not found.");
        }
    }

    @Override
    public OrdersResponse findByIdOrder(Integer id) {
        String sql = "SELECT o.order_id, o.total, o.tracking_number, o.status, " +
                "o.shipping_address, o.payment_method, " +
                "o.created_at, o.updated_at, o.deleted_at, " +
                "o.user_id, u.fullname, u.phone, u.email, " +
                "oi.order_item_id, oi.quantity, oi.price, b.title " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "JOIN order_items oi ON oi.order_id = o.order_id " +
                "JOIN books b ON b.book_id = oi.book_id " +
                "WHERE o.order_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new OrdersRowMapper2(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotfoundException("Order with IDs " + id + " not found.");
        }
    }

    @Override
    public OrdersRequest save(OrdersRequest ordersRequest) {
        String satus = OrderStatus.CREATED.name();
        String trackingNumber = UUID.randomUUID().toString();
        String sql = "INSERT INTO orders (total, tracking_number, status, shipping_address, " +
                "payment_method,user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";


        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, ordersRequest.getTotal());
            ps.setString(2, trackingNumber);
            ps.setString(3, satus);
            ps.setString(4, ordersRequest.getShippingAddress());
            ps.setString(5, ordersRequest.getPaymentMethod());
            ps.setInt(6, ordersRequest.getUserId());
            return ps;
        }, keyHolder);

        ordersRequest.setOrderId(keyHolder.getKey().intValue());

        return ordersRequest;
    }

    @Override
    public OrdersRequest update(Integer id, OrdersRequest ordersRequest) {
        String sql = "UPDATE orders SET total = ?, tracking_number = ?, status = ?, " +
                "shipping_address = ?, payment_method = ? " +
                "WHERE order_id = ?";

        jdbcTemplate.update(sql,
                ordersRequest.getTotal(),
                ordersRequest.getTrackingNumber(),
                ordersRequest.getStatus(),
                ordersRequest.getShippingAddress(),
                ordersRequest.getPaymentMethod(),
                id);

        ordersRequest.setOrderId(id);
        return ordersRequest;
    }

    @Override
    public void delete(Integer id) {
        String sql = "UPDATE orders SET deleted_at = true WHERE order_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
        double total = 0.0;

        // Kiểm tra các mục đặt hàng không rỗng
        if (ordersRequest.getOrdersItemsRequests() == null || ordersRequest.getOrdersItemsRequests().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        // Kiểm tra số lượng sách có sẵn
        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            ApiResponse<Integer> availableQuantity = apiBooksClient.getAvailableQuantity(item.getBookId());
            if (item.getQuantity() > availableQuantity.getData()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock for book ID: " + item.getBookId());
            }
            total += item.getQuantity() * item.getPrice();
        }

        // Lưu tổng tiền và thông tin đơn hàng
        ordersRequest.setTotal(total);

        // Thực hiện lưu đơn hàng
        String sqlOrder = "INSERT INTO orders (total, tracking_number, status, shipping_address, " +
                "payment_method, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, ordersRequest.getTotal());
            ps.setString(2, generateOrderCode());
            ps.setString(3, OrderStatus.CREATED.name());
            ps.setString(4, ordersRequest.getShippingAddress());
            ps.setString(5, PaymentStatus.PENDING.name());
            ps.setInt(6, ordersRequest.getUserId());
            return ps;
        }, keyHolder);

        // Lấy ID của đơn hàng
        int orderId = keyHolder.getKey().intValue();
        ordersRequest.setOrderId(orderId);

        // Thêm các mục đơn hàng và lấy ID của mỗi mục
        String sqlOrderItem = "INSERT INTO order_items (order_id, book_id, quantity, price, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            KeyHolder itemKeyHolder = new GeneratedKeyHolder();  // Tạo KeyHolder riêng cho mỗi mục
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlOrderItem, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, orderId);
                ps.setInt(2, item.getBookId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getPrice());
                ps.setString(5, OrderItemStatus.PENDING.name());
                return ps;
            }, itemKeyHolder);

            // Lấy ID của từng order item và gán lại cho từng item
            int orderItemId = itemKeyHolder.getKey().intValue();
            item.setOrderItemId(orderItemId);  // Gán ID của order item cho đối tượng item
            item.setStatus(OrderItemStatus.PENDING);
        }

        // Giảm số lượng sách trong kho sau khi tất cả đều được kiểm tra và lưu
        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
            apiBooksClient.decreaseQuantity(item.getBookId(), item.getQuantity());
        }

        // Cập nhật trạng thái đơn hàng
        ordersRequest.setStatus(OrderStatus.CREATED);
        ordersRequest.setTrackingNumber(generateOrderCode());
        return ordersRequest;
    }

    @Override
    public OrdersRequest updateOrder(int orderId, OrdersRequest updatedOrderRequest) {
        if (updatedOrderRequest == null || updatedOrderRequest.getOrdersItemsRequests() == null) {
            throw new IllegalArgumentException("Updated order request cannot be null or empty.");
        }

        // Lấy thông tin đơn hàng hiện tại
        String sqlGetOrder = "SELECT total, shipping_address FROM orders WHERE order_id = ?";
        OrdersResponse existingOrderResponse = jdbcTemplate.queryForObject(sqlGetOrder, new RowMapper<OrdersResponse>() {
            @Override
            public OrdersResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrdersResponse order = new OrdersResponse();
                order.setTotal(rs.getDouble("total"));
                order.setShippingAddress(rs.getString("shipping_address"));
                return order;
            }
        }, orderId);

        // Cập nhật địa chỉ giao hàng nếu có thay đổi
        if (!existingOrderResponse.getShippingAddress().equals(updatedOrderRequest.getShippingAddress())) {
            String sqlUpdateShippingAddress = "UPDATE orders SET shipping_address = ? WHERE order_id = ?";
            jdbcTemplate.update(sqlUpdateShippingAddress, updatedOrderRequest.getShippingAddress(), orderId);
        }

        // Lấy các mục trong đơn hàng hiện tại
        String sqlGetOrderItems = "SELECT book_id, quantity, price FROM order_items WHERE order_id = ?";
        List<OrdersItemsResponse> existingOrderItems = jdbcTemplate.query(sqlGetOrderItems, new RowMapper<OrdersItemsResponse>() {
            @Override
            public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrdersItemsResponse item = new OrdersItemsResponse();
                item.setBookId(rs.getInt("book_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                return item;
            }
        }, orderId);

        // Tạo một bản đồ để tra cứu số lượng hiện tại và giá của các mục
        Map<Integer, OrdersItemsResponse> currentOrderItemsMap = existingOrderItems.stream()
                .collect(Collectors.toMap(OrdersItemsResponse::getBookId, item -> item));

        String sqlUpdateOrderItem = "UPDATE order_items SET quantity = ?, price = ? WHERE order_id = ? AND book_id = ?";
        String sqlInsertOrderItem = "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
        String sqlDeleteOrderItem = "DELETE FROM order_items WHERE order_id = ? AND book_id = ?";

        // Cập nhật hoặc thêm các mục trong đơn hàng
        for (OrdersItemsRequest updatedItem : updatedOrderRequest.getOrdersItemsRequests()) {
            int quantityDifference = 0;

            // Kiểm tra số lượng yêu cầu với số lượng có sẵn trong kho
            ApiResponse<Integer> availableQuantity = apiBooksClient.getAvailableQuantity(updatedItem.getBookId());
            int currentQuantityInOrder = currentOrderItemsMap.getOrDefault(updatedItem.getBookId(), new OrdersItemsResponse()).getQuantity();

            // Tính toán số lượng tối đa có thể yêu cầu: Số lượng hiện tại + Số lượng còn trong kho
            int maxPossibleQuantity = currentQuantityInOrder + availableQuantity.getData();

            // Kiểm tra nếu số lượng yêu cầu vượt quá số lượng tối đa có thể yêu cầu
            if (updatedItem.getQuantity() > maxPossibleQuantity) {
                throw new IllegalArgumentException("Requested quantity exceeds the maximum available stock for book ID: " + updatedItem.getBookId());
            }

            // Kiểm tra nếu số lượng yêu cầu là âm, ném ngoại lệ
            if (updatedItem.getQuantity() < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative for book ID: " + updatedItem.getBookId());
            }

            if (currentOrderItemsMap.containsKey(updatedItem.getBookId())) {
                // Nếu mục đã tồn tại, tính toán sự thay đổi số lượng
                quantityDifference = updatedItem.getQuantity() - currentQuantityInOrder;

                // Cập nhật số lượng trong cơ sở dữ liệu và điều chỉnh kho
                if (quantityDifference != 0) {
                    if (quantityDifference > 0) {
                        // Giảm số lượng trong kho khi tăng số lượng trong đơn hàng
                        apiBooksClient.decreaseQuantity(updatedItem.getBookId(), quantityDifference);
                    } else {
                        // Tăng số lượng trong kho khi giảm số lượng trong đơn hàng
                        apiBooksClient.increaseQuantity(updatedItem.getBookId(), -quantityDifference);
                    }

                    // Cập nhật số lượng trong cơ sở dữ liệu
                    jdbcTemplate.update(sqlUpdateOrderItem, updatedItem.getQuantity(), updatedItem.getPrice(), orderId, updatedItem.getBookId());
                }
            } else {
                // Nếu mục chưa tồn tại, thêm mới vào cơ sở dữ liệu
                jdbcTemplate.update(sqlInsertOrderItem, orderId, updatedItem.getBookId(), updatedItem.getQuantity(), updatedItem.getPrice());
                // Giảm số lượng trong kho khi thêm mới
                apiBooksClient.decreaseQuantity(updatedItem.getBookId(), updatedItem.getQuantity());
            }

            // Nếu số lượng được cập nhật về 0, xóa mục trong đơn hàng
            if (updatedItem.getQuantity() == 0) {
                jdbcTemplate.update(sqlDeleteOrderItem, orderId, updatedItem.getBookId());
                // Tăng lại số lượng trong kho nếu xóa item
                apiBooksClient.increaseQuantity(updatedItem.getBookId(), currentQuantityInOrder);
            }
        }

        // Tính toán tổng tiền mới
        double newTotal = updatedOrderRequest.getOrdersItemsRequests().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Cập nhật tổng tiền trong cơ sở dữ liệu
        if (newTotal > 0) {
            String sqlUpdateTotal = "UPDATE orders SET total = ? WHERE order_id = ?";
            jdbcTemplate.update(sqlUpdateTotal, newTotal, orderId);
        } else {
            throw new IllegalArgumentException("Total cannot be zero or negative");
        }

        // Gán tổng tiền vào đối tượng trả về
        updatedOrderRequest.setTotal(newTotal);

        return updatedOrderRequest;
    }




    @Override
    public void deleteOrder(Integer orderId) {
        // Bước 1: Lấy danh sách order items liên quan đến order
        String sqlGetOrderItems = "SELECT book_id, quantity FROM order_items WHERE order_id = ?";
        List<OrdersItemsResponse> orderItems = jdbcTemplate.query(sqlGetOrderItems, new RowMapper<OrdersItemsResponse>() {
            @Override
            public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrdersItemsResponse ordersItemsResponse=new OrdersItemsResponse();
                ordersItemsResponse.setBookId(rs.getInt("book_id"));
                ordersItemsResponse.setQuantity(rs.getInt("quantity"));
                return ordersItemsResponse;
            }
        }, orderId);

        // Kiểm tra nếu không tìm thấy order items
        if (orderItems.isEmpty()) {
            throw new RuntimeException("No order items found for order ID: " + orderId);
        }

        // Bước 2: Cập nhật số lượng sách trong kho
        for (OrdersItemsResponse item : orderItems) {
            try {
                apiBooksClient.increaseQuantity(item.getBookId(), item.getQuantity()); // Sử dụng số lượng dương để tăng lại
            } catch (Exception e) {
                throw new RuntimeException("Failed to update quantity for book ID: " + item.getBookId(), e);
            }
        }

        String sqlDeleteOrderItems = "DELETE FROM order_items WHERE order_id = ?";
        int rowsAffected = jdbcTemplate.update(sqlDeleteOrderItems, orderId);


        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to delete order items for order ID: " + orderId);
        }


        String sqlDeleteOrder = "DELETE FROM orders WHERE order_id = ?";
        rowsAffected = jdbcTemplate.update(sqlDeleteOrder, orderId);

        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to delete order with ID: " + orderId);
        }
    }

    @Override
    public void updateOrdersStatus(Integer idOrder,String status){
        String sqlUpdateOrderStatus = "UPDATE orders SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sqlUpdateOrderStatus, status, idOrder);
    }




//    @Override
//    public void deleteOrderItem(Integer orderItemId) {
//        // Bước 1: Lấy thông tin order_item từ cơ sở dữ liệu (book_id và quantity)
//        String sqlGetOrderItem = "SELECT book_id, quantity FROM order_items WHERE order_item_id = ?";
//        OrdersItemsResponse orderItem = jdbcTemplate.queryForObject(sqlGetOrderItem, new OrdersItemsRowMapper());
//
//        // Bước 2: Xóa order_item khỏi bảng order_items
//        String sqlDeleteOrderItem = "DELETE FROM order_items WHERE order_item_id = ?";
//        jdbcTemplate.update(sqlDeleteOrderItem, orderItemId);
//
//        // Bước 3: Cập nhật số lượng sách trong microservice quản lý sách
//        apiBooksClient.increaseQuantity(orderItem.getBookId(), orderItem.getQuantity());
//    }


}


//    @Override
//    public OrdersRequest createOrder(OrdersRequest ordersRequest) {
//
//        double total = 0.0;
//
//        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
//            total += item.getQuantity() * item.getPrice();
//        }
//        ordersRequest.setTotal(total);
//
//        String sqlOrder = "INSERT INTO orders (total, tracking_number, status, shipping_address, " +
//                "payment_method, payment_status, user_id) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
//            ps.setDouble(1, ordersRequest.getTotal());
//            ps.setString(2, generateOrderCode());
//            ps.setString(3, OrderStatus.PENDING.name());
//            ps.setString(4, ordersRequest.getShippingAddress());
//            ps.setString(5, ordersRequest.getPaymentMethod());
//            ps.setString(6, PaymentStatus.PENDING.name());
//            ps.setInt(7, ordersRequest.getUserId());
//            return ps;
//        }, keyHolder);
//
//
//        int orderId = keyHolder.getKey().intValue();
//        ordersRequest.setOrderId(orderId);
//
//        String sqlOrderItem = "INSERT INTO order_items (order_id, book_id, quantity, price, status) " +
//                "VALUES (?, ?, ?, ?, ?)";
//
//        for (OrdersItemsRequest item : ordersRequest.getOrdersItemsRequests()) {
//            jdbcTemplate.update(sqlOrderItem, orderId, item.getBookId(), item.getQuantity(), item.getPrice(), OrderStatus.PENDING.name());
//            item.setStatus(OrderStatus.PENDING.name());
//        }
//        ordersRequest.setStatus(OrderStatus.PENDING.name());
//        ordersRequest.setTrackingNumber(generateOrderCode());
//        ordersRequest.setTotal(total);
//        ordersRequest.setPaymentStatus(PaymentStatus.PENDING.name());
//        return ordersRequest;
//    }
