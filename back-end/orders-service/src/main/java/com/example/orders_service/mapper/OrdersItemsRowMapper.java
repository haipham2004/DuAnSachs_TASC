package com.example.orders_service.mapper;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersItemsRowMapper implements RowMapper<OrdersItemsResponse> {
    @Override
    public OrdersItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrdersItemsResponse ordersItemsResponse=new OrdersItemsResponse();
        ordersItemsResponse.setOrderItemId(rs.getInt("order_item_id"));
        ordersItemsResponse.setQuantity(rs.getInt("quantity"));
        ordersItemsResponse.setPrice(rs.getDouble("price"));
        ordersItemsResponse.setStatus(rs.getString("status"));
        ordersItemsResponse.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        ordersItemsResponse.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        ordersItemsResponse.setDeletedAt(rs.getBoolean("deleted_at"));
        ordersItemsResponse.setOrderId(rs.getInt("order_id"));
        ordersItemsResponse.setBookId(rs.getInt("book_id"));
        ordersItemsResponse.setTileBook(rs.getString("title"));
        return ordersItemsResponse;
    }
}
