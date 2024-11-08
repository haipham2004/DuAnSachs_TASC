package com.example.books_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksRequest {

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
