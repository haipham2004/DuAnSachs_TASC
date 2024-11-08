package com.example.books_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Books{

    private Integer bookId;

    private String title;

    private Integer authorId;

    private Integer publisherId;

    private Integer categoryId;

    private Double price;

    private String description;

    private Integer stock;

    private Integer quantity;

    private String status;

    private String imageUrl;

    private String thumbnail;

}


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
//@Table(name="books")
//public class Books {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "book_id")
//    private Integer bookId;
//
//    @Column(name = "title", length = 255)
//    private String title;
//
//    @Column(name = "author_id")
//    private Integer authorId;
//
//    @Column(name = "publisher_id")
//    private Integer publisherId;
//
//    @Column(name = "category_id")
//    private Integer categoryId;
//
//    @Column(name = "price")
//    private Double price;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "stock", columnDefinition = "INT DEFAULT 0")
//    private Integer stock;
//
//    @Column(name = "quantity", columnDefinition = "INT DEFAULT 0")
//    private Integer quantity;
//
//    @Column(name = "status", length = 100)
//    private String status;
//
//    @Column(name = "image_url")
//    private String imageUrl;
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
