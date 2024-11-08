//package com.example.books_service.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name="publishers")
//public class Publishers {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "publisher_id")
//    private Integer publisherId;
//
//    @Column(name = "name", length = 100, unique = true, nullable = false)
//    private String name;
//
//    @Column(name = "address", length = 255)
//    private String address;
//
//    @Column(name = "phone", length = 20)
//    private String phone;
//
//    @Column(name = "email", length = 100)
//    private String email;
//
//    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "deleted_at", columnDefinition = "BIT DEFAULT 0")
//    private Boolean deletedAt;
//}
