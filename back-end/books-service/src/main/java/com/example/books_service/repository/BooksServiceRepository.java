package com.example.books_service.repository;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;

import java.util.List;

public interface BooksServiceRepository {

     List<BooksResponse> findAllBooksDto();

     BooksResponse findById(Integer id);

     BooksRequest save(BooksRequest booksRequest);

    BooksRequest update(BooksRequest booksRequest, Integer id);

    void deleteById(boolean delete, Integer id);

    PageResponse<BooksResponse> findBooksPage(int pageNumber, int pageSize);

    PageResponse<BooksResponse> findBooksPage2(int pageNumber, int pageSize, String filter);

}
