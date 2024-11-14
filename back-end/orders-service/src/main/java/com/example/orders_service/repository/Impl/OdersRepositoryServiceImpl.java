package com.example.orders_service.repository.Impl;

import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.mapper.OrdersRowMapper;
import com.example.orders_service.repository.OrdersRepositoryService;
import com.example.orders_service.service.OrdersItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OdersRepositoryServiceImpl implements OrdersRepositoryService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public OdersRepositoryServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<OrdersResponse> fillAll() {

        String sql = "SELECT " +
                "o.order_id, o.total, o.tracking_number, o.status, " +
                "o.shipping_address, o.payment_method, o.payment_status, " +
                "o.created_at, o.updated_at, o.deleted_at, " +
                "o.user_id, u.fullname, u.phone " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id";
        return namedParameterJdbcTemplate.query(sql, new OrdersRowMapper());
    }
}
