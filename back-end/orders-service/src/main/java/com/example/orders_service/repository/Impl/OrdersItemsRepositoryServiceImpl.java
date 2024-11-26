package com.example.orders_service.repository.Impl;


import com.example.orders_service.client.ApiBooksClient;
import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.OrderItemStatus;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.mapper.OrdersItemsRowMapper;
import com.example.orders_service.repository.OrdersItemsRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Repository
public class OrdersItemsRepositoryServiceImpl implements OrdersItemsRepositoryService {


    private final JdbcTemplate jdbcTemplate;

    private ApiBooksClient apiBooksClient;

    @Autowired
    public OrdersItemsRepositoryServiceImpl(JdbcTemplate jdbcTemplate, ApiBooksClient apiBooksClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.apiBooksClient = apiBooksClient;
    }

    private OrdersItemsRowMapper getOrdersMapper() {
        return new OrdersItemsRowMapper();
    }

    @Override
    public List<OrdersItemsResponse> findAll() {
        String sql = "SELECT oi.order_item_id, oi.quantity, oi.price, oi.status, oi.created_at, oi.updated_at, oi.deleted_at, " +
                "oi.order_id, oi.book_id, b.title, oi.quantity * oi.price as totals " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "JOIN books b ON oi.book_id = b.book_id " +
                "WHERE oi.deleted_at = 0";

        return jdbcTemplate.query(sql, new OrdersItemsRowMapper());
    }

    @Override
    public List<OrdersItemsResponse> getOrderWithItems(int orderId) {

        String sql = "SELECT oi.order_item_id, oi.quantity, oi.price, oi.status, oi.created_at, oi.updated_at, oi.deleted_at, " +
                "oi.order_id, oi.book_id, b.title, oi.quantity * oi.price as totals " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "JOIN books b ON oi.book_id = b.book_id " +
                "WHERE oi.order_id = ? AND oi.deleted_at = 0";

        List<OrdersItemsResponse> orderItems = jdbcTemplate.query(sql, getOrdersMapper(),orderId  );

        return orderItems;
    }

    @Override
    public OrdersItemsResponse findById(Integer id) {
        String sql = "SELECT oi.order_item_id, oi.quantity, oi.price, oi.status, oi.created_at, oi.updated_at, oi.deleted_at, " +
                "oi.order_id, oi.book_id, b.title, oi.quantity * oi.price as totals " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "JOIN books b ON oi.book_id = b.book_id " +
                "WHERE oi.order_item_id = ? AND oi.deleted_at = 0";

        return jdbcTemplate.queryForObject(sql, getOrdersMapper(), id);
    }


    @Override
    public OrdersItemsRequest save(OrdersItemsRequest ordersItemsRequest) {
        String sql = "INSERT INTO order_items (order_id, book_id, quantity, price, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ordersItemsRequest.getOrderId());
            ps.setInt(2, ordersItemsRequest.getBookId());
            ps.setInt(3, ordersItemsRequest.getQuantity());
            ps.setDouble(4, ordersItemsRequest.getPrice());
            ps.setString(5, OrderItemStatus.PENDING.name());
            return ps;
        }, keyHolder);

        ordersItemsRequest.setOrderItemId(keyHolder.getKey().intValue());

        return ordersItemsRequest;
    }


    @Override
    public OrdersItemsRequest update(Integer id, OrdersItemsRequest ordersItemsRequest) {
        String sql = "UPDATE order_items SET quantity = ?, price = ?, status = ? WHERE order_item_id = ?";
        jdbcTemplate.update(sql,
                ordersItemsRequest.getQuantity(),
                ordersItemsRequest.getPrice(),
                ordersItemsRequest.getStatus(),
                id
        );
        ordersItemsRequest.setOrderItemId(id);
        return ordersItemsRequest;
    }


    @Override
    public void delete(Integer id) {

        String sql = "UPDATE order_items SET deleted_at = true WHERE order_item_id = ?";

        jdbcTemplate.update(sql, id);
    }



    @Override
    public void deleteOrderItem(Integer orderItemId) {
        // Bước 1: Lấy thông tin order_item từ cơ sở dữ liệu (book_id và quantity)
        String sqlGetOrderItem = "SELECT book_id, quantity FROM order_items WHERE order_item_id = ?";
        OrdersItemsResponse orderItem = jdbcTemplate.queryForObject(sqlGetOrderItem, new RowMapper<OrdersItemsResponse>() {
            @Override
            public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrdersItemsResponse ordersItemsResponse=new OrdersItemsResponse();
                ordersItemsResponse.setBookId(rs.getInt("book_id"));
                ordersItemsResponse.setQuantity(rs.getInt("quantity"));
                return ordersItemsResponse;
            }
        }, orderItemId);

        String sqlDeleteOrderItem = "DELETE FROM order_items WHERE order_item_id = ?";
        jdbcTemplate.update(sqlDeleteOrderItem, orderItemId);

        apiBooksClient.increaseQuantity(orderItem.getBookId(), orderItem.getQuantity());
    }

    @Override
    public void updateOrderItem(int orderItemId, int newQuantity) {
        // Lấy thông tin order item
        String sqlGetOrderItem = "SELECT book_id, quantity, price FROM order_items WHERE order_item_id = ?";
        OrdersItemsResponse orderItem = jdbcTemplate.queryForObject(sqlGetOrderItem, new RowMapper<OrdersItemsResponse>() {
            @Override
            public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrdersItemsResponse ordersItemsResponse = new OrdersItemsResponse();
                ordersItemsResponse.setBookId(rs.getInt("book_id"));
                ordersItemsResponse.setQuantity(rs.getInt("quantity"));
                ordersItemsResponse.setPrice(rs.getDouble("price"));
                return ordersItemsResponse;
            }
        },orderItemId);

        // Tính toán sự khác biệt giữa số lượng mới và số lượng hiện tại
        int quantityDifference = newQuantity - orderItem.getQuantity();

        // Cập nhật số lượng sách trong kho
        if (quantityDifference > 0) {
            // Nếu số lượng mới lớn hơn số lượng hiện tại, giảm số lượng trong kho
            apiBooksClient.decreaseQuantity(orderItem.getBookId(), quantityDifference);
        } else if (quantityDifference < 0) {
            // Nếu số lượng mới nhỏ hơn số lượng hiện tại, tăng số lượng trong kho
            apiBooksClient.increaseQuantity(orderItem.getBookId(), -quantityDifference); // Lưu ý rằng quantity cần phải là dương
        }

        // Cập nhật số lượng order item
        String sqlUpdateOrderItem = "UPDATE order_items SET quantity = ? WHERE order_item_id = ?";
        jdbcTemplate.update(sqlUpdateOrderItem, newQuantity, orderItemId);

        // Cập nhật lại tổng tiền cho đơn hàng
        String sqlGetOrderId = "SELECT order_id FROM order_items WHERE order_item_id = ?";
        int orderId = jdbcTemplate.queryForObject(sqlGetOrderId, Integer.class,orderItemId);

        // Cập nhật tổng tiền cho đơn hàng
        String sqlUpdateOrderTotal = "UPDATE orders SET total = (SELECT SUM(quantity * price) FROM order_items WHERE order_id = ?) WHERE order_id = ?";
        jdbcTemplate.update(sqlUpdateOrderTotal, orderId, orderId);
    }
}
