package com.example.orders_service.entity;


import com.example.orders_service.dto.response.OrderItemStatus;
import com.example.orders_service.dto.response.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;


    @Column(name = "book_id", nullable = false)
    private int bookId;

    private Integer quantity;

    private Double price;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)  // Lưu giá trị Enum dưới dạng chuỗi
    @Column(name = "status", nullable = false, unique = true, length = 50)
    private OrderItemStatus status;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "BIT(0) DEFAULT 0")
    private boolean deletedAt = true;


}

//    private int orderItemId;
//
//    private int orderId;
//
//    private int bookId;
//
//    private int quantity;
//
//    private Double price;
//
//    private OrderStatus status;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    private Boolean deletedAt;
