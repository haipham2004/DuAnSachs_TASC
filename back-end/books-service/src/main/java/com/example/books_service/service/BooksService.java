package com.example.books_service.service;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.request.BooksRequest2;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.BooksResponse2;
import com.example.books_service.dto.response.PageResponse;

import java.util.List;

public interface BooksService {

    List<BooksResponse> findAllBooksDto();

    PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset);

    PageResponse<BooksResponse2> findAllBooksPage2(int pageSize, int offset);

    BooksResponse findById(Integer id);

    BooksRequest save(BooksRequest booksRequest);

    BooksRequest2 save2(BooksRequest2 booksRequest2);

    BooksRequest update(BooksRequest booksRequest, Integer id);

    void deleteById(Integer id);
}
