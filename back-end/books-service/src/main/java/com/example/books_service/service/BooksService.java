package com.example.books_service.service;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;

import java.util.List;

public interface BooksService {

    List<BooksResponse> findAllBooksDto();

    PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset);

    BooksResponse findById(Integer id);

    BooksRequest save(BooksRequest booksRequest);

    BooksRequest update(BooksRequest booksRequest, Integer id);

    void deleteById(boolean delete, Integer id);

    public PageResponse<BooksResponse> findBooksPage2(int pageNumber, int pageSize, String filter);
}
