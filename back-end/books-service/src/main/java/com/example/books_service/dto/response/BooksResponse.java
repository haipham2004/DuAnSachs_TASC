package com.example.books_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksResponse {

    private Integer bookId;

    private String title;

    private String nameAuthor;

    private String namePublisher;

    private String nameCategory;

    private Double price;

    private String description;

    private Integer stock;

    private Integer quantity;

    private String status;

    private List<String> imageUrl;

    private String thumbnail;
}
