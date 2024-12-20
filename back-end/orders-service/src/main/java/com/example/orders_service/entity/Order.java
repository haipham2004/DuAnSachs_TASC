package com.example.orders_service.entity;

import com.example.orders_service.dto.response.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name="user_id")
    private int userId;  // Lớp User phải được ánh xạ ở đây

//    @Column(name = "order_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime orderDate;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "BIT(0) DEFAULT 0")
    private boolean deletedAt = true;

}


//    private int orderId;
//
//    private int userId;
//
//    private Double total;
//
//    private String trackingNumber;
//
//    private OrderStatus status;
//
//    private String shippingAddress;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    private Boolean deletedAt;