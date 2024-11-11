package com.example.books_service.service.imp;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.repository.BooksServiceRepository;
import com.example.books_service.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksServiceImp implements BooksService {

    private BooksServiceRepository booksServiceRepository;

    @Autowired
    public BooksServiceImp(BooksServiceRepository booksServiceRepository) {
        this.booksServiceRepository = booksServiceRepository;
    }

    @Override
    public PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset) {
        return booksServiceRepository.findBooksPage(pageSize, offset);
    }

    @Override
    public BooksResponse findById(Integer id) {
        return booksServiceRepository.findById(id);
    }

    @Override
    public BooksRequest save(BooksRequest booksRequest) {
        return booksServiceRepository.save(booksRequest);
    }


    @Override
    public BooksRequest update(BooksRequest booksRequest, Integer id) {
        return booksServiceRepository.update(booksRequest, id);
    }


    @Override
    public void deleteById(boolean delete, Integer id) {
        booksServiceRepository.deleteById(delete,id);
    }


    @Override
    public PageResponse<BooksResponse> findBooksPage3(String nameBook, String nameAuthor, String namePublisher,
                                                      String nameCategory, double priceMin, double priceMax,
                                                      int pageNumber, int pageSize, String sort) {
        return booksServiceRepository.findBooksPage3( nameBook,  nameAuthor,  namePublisher,  nameCategory, priceMin, priceMax,  pageNumber,  pageSize, sort );
    }


}
