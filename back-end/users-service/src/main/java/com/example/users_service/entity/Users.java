package com.example.users_service.entity;

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
public class Users  {

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

    @Column(name="address",nullable = true, length = 120)
    private String addRess;

    @Column(name = "role_id", nullable = false)
    private Integer idRoles;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "BIT(0) DEFAULT 0")
    private boolean deletedAt = true;

    public Users(String username, String email, String phone, String password, String fullName, String addRess) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName=fullName;
        this.addRess=addRess;
    }

}
