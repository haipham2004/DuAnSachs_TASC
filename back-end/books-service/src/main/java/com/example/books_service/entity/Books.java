package com.example.books_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private Double consPrice;

    private Integer quantity;

    private String status;

    private String imageUrl;

    private List<String> thumbnail;

}

