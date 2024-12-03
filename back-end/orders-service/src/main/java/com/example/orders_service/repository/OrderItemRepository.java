package com.example.orders_service.repository;

import com.example.common_service.event.OrderItem;
import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,Integer> {

    @Query("SELECT new com.example.orders_service.dto.response.OrdersItemsResponse(oi.orderItemId, oi.quantity, oi.price, oi.orderId, oi.bookId) " +
            "FROM OrderItems oi " +
            "WHERE oi.orderId IN :orderIds")
    List<OrdersItemsResponse> findOrderItemsByOrderIds(@Param("orderIds") List<Integer> orderIds);

    @Query("SELECT new com.example.orders_service.dto.response.OrdersItemsResponse(oi.orderItemId, oi.quantity, oi.price, oi.orderId,oi.bookId) " +
            "FROM OrderItems oi " +
            "WHERE oi.orderId = :orderId")
    List<OrdersItemsResponse> findOrderItemsByOrderId(@Param("orderId") Integer orderId);

}
