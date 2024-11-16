package com.example.orders_service.repository.Impl;

import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.mapper.OrdersRowMapper;
import com.example.orders_service.repository.OrdersRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OdersRepositoryServiceImpl implements OrdersRepositoryService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OdersRepositoryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private OrdersRowMapper getOrdersMapper() {
        return new OrdersRowMapper();
    }

    @Override
    public PageResponse<OrdersResponse> fillAll(String fullName, String phone, int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;

        String sql = "SELECT " +
                "o.order_id, o.total, o.tracking_number, o.status, " +
                "o.shipping_address, o.payment_method, o.payment_status, " +
                "o.created_at, o.updated_at, o.deleted_at, " +
                "o.user_id, u.fullname, u.phone " +
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

}

