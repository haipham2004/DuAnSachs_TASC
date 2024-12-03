package com.example.orders_service.repository;

import com.example.orders_service.dto.response.OrdersItemsResponse;
import com.example.orders_service.dto.response.OrdersResponse;
import com.example.orders_service.dto.response.PageResponse;
import com.example.orders_service.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT new com.example.orders_service.dto.response.OrdersResponse(" +
            "o.orderId, o.total, o.trackingNumber, o.status, o.shippingAddress, o.userId, " +
            "u.fullName, u.phone, u.email) " +
            "FROM Order o " +
            "LEFT JOIN Users u ON o.userId = u.userId " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE %:fullName%) " +
            "AND (:phone IS NULL OR u.phone LIKE %:phone%)")
    Page<OrdersResponse> findAllOrdersWithSearch(Pageable pageable, String fullName, String phone);

    @Query("SELECT new com.example.orders_service.dto.response.OrdersResponse(" +
            "o.orderId, o.total, o.trackingNumber, o.status, o.shippingAddress, o.userId, " +
            "u.fullName, u.phone, u.email) " +
            "FROM Order o " +
            "LEFT JOIN Users u ON o.userId = u.userId " +
            "WHERE o.orderId = :id")
    OrdersResponse findOrderById(@Param("id") Integer id);


}
