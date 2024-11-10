package com.example.books_service.repository;

import com.example.books_service.dto.response.AuthorsResponse;

import java.util.List;

public interface AuthorsServiceRepository {
    List<AuthorsResponse> findAllAuthorsDto();
}
