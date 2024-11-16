package com.example.orders_service.repository.Impl;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.mapper.OrdersItemsRowMapper;
import com.example.orders_service.mapper.OrdersRowMapper;
import com.example.orders_service.repository.OrdersItemsRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersItemsRepositoryServiceImpl implements OrdersItemsRepositoryService {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrdersItemsRepositoryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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


}
