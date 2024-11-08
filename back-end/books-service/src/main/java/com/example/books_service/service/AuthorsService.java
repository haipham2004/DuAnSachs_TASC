package com.example.books_service.service;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.BooksResponse;

import java.util.List;

public interface AuthorsService {

    List<AuthorsResponse> findAllAuthorsDto();
}
