package com.example.orders_service.mapper;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersRowMapper2 implements RowMapper<OrdersResponse> {

    @Override
    public OrdersResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrdersResponse ordersResponse = new OrdersResponse();

        // Mapping orders fields
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

        // Initialize list for order items
        List<OrdersItemsResponse> orderItemResponses = new ArrayList<>();

        do {
            // Mapping order items
            OrdersItemsResponse item = new OrdersItemsResponse();
            item.setOrderItemId(rs.getInt("order_item_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getDouble("price"));
            item.setTileBook(rs.getString("title"));
            item.setBookId(rs.getInt("book_id"));
            item.setOrderId(rs.getInt("order_id"));
            orderItemResponses.add(item);
        } while (rs.next() && rs.getInt("order_id") == ordersResponse.getOrderId());

        // Set order items to orders response
        ordersResponse.setOrdersItems(orderItemResponses);

        return ordersResponse;
    }
}
