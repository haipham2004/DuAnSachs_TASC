package com.example.orders_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@ToString
@Entity
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Column(name="username",nullable = false, unique = true, length = 20)
    private String username;

    @Column(name="email",nullable = false, unique = true, length = 50)
    private String email;

    @Column(name="phone",nullable = false, unique = true, length = 50)
    private String phone;

    @Column(name="password",nullable = false, length = 120)
    private String password;

    @Column(name="fullname",nullable = true, length = 120)
    private String fullName;

}
