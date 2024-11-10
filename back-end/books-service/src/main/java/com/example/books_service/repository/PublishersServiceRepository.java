package com.example.books_service.repository;

import com.example.books_service.dto.response.PublishersResponse;

import java.util.List;

public interface PublishersServiceRepository {
    List<PublishersResponse> findAllCategoriesDto();
}
