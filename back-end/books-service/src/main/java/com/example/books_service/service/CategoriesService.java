package com.example.books_service.service;

import com.example.books_service.dto.response.CategoriesResponse;
import java.util.List;

public interface CategoriesService {

    List<CategoriesResponse> findAllCategoriesDto();
}