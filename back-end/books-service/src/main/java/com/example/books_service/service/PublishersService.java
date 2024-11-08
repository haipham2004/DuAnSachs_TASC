package com.example.books_service.service;

import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PublishersResponse;

import java.util.List;

public interface PublishersService {

    List<PublishersResponse> findAllPublisherDto();
}
