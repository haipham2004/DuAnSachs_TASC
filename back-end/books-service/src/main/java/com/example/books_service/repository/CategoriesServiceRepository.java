package com.example.books_service.repository;

import com.example.books_service.dto.response.CategoriesResponse;

import java.util.List;

public interface CategoriesServiceRepository {
    List<CategoriesResponse> findAllCategoriesDto();
}
