package com.example.books_service.repository;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;

import java.util.List;

public interface BooksServiceRepository {

    BooksResponse findById(Integer id);

    BooksRequest save(BooksRequest booksRequest);

    BooksRequest update(BooksRequest booksRequest, Integer id);

    void deleteById(boolean delete, Integer id);

    PageResponse<BooksResponse> findBooksPage(int pageNumber, int pageSize);


    PageResponse<BooksResponse> findBooksPage3(String nameBook, String nameAuthor, String namePublisher,
                                               String nameCategory, double priceMin, double priceMax, int pageNumber, int pageSize, String sort);

    List<BooksResponse> getAvailableQuantity(List<Integer> bookIds);

    void decreaseQuantity(Integer bookId, Integer quantity);

    void increaseQuantity(Integer bookId, Integer quantity);

    List<BooksResponse> reduceQuantitys(Integer bookId, Integer quantity);

    List<BooksResponse> increaseQuantitys(Integer bookId, Integer quantity);

}
