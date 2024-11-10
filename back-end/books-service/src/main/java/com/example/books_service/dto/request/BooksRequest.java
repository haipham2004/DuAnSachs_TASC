package com.example.books_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private Double consPrice;

    private String description;

    private Integer quantity;

    private String status;

    private List<String> imageUrl;

    private String thumbnail;

}
