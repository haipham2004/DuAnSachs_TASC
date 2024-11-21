package com.example.orders_service.mapper;

import com.example.orders_service.dto.response.OrdersResponse;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersRowMapper implements RowMapper<OrdersResponse> {
    @Override
    public OrdersResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrdersResponse ordersResponse=new OrdersResponse();
        ordersResponse.setOrderId(rs.getInt("order_id"));
        ordersResponse.setTotal(rs.getDouble("total"));
        ordersResponse.setTrackingNumber(rs.getString("tracking_number"));
        ordersResponse.setStatus(rs.getString("status"));
        ordersResponse.setShippingAddress(rs.getString("shipping_address"));
        ordersResponse.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        ordersResponse.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        ordersResponse.setDeletedAt(rs.getBoolean("deleted_at"));
        ordersResponse.setUserId(rs.getInt("user_id"));
        ordersResponse.setFullNameUsers(rs.getString("fullname"));
        ordersResponse.setPhoneUsers(rs.getString("phone"));
        ordersResponse.setEmailUser(rs.getString("email"));
        return ordersResponse;
    }
}
