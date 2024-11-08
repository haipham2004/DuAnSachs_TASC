package com.example.books_service.service.imp;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.mapper.BooksMapper;
import com.example.books_service.repository.BooksRepository;
import com.example.books_service.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksServiceImp implements BooksService {

    private BooksRepository booksRepository;

    private BooksMapper booksMapper;

    @Autowired
    public BooksServiceImp(BooksRepository booksRepository, BooksMapper booksMapper) {
        this.booksRepository = booksRepository;
        this.booksMapper = booksMapper;
    }

    @Override
    public List<BooksResponse> findAllBooksDto() {
        return booksRepository.findAllBooksDto();
    }

    @Override
    public PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset) {
        return booksRepository.findBooksPage(pageSize, offset);
    }

    @Override
    public BooksResponse findById(Integer id) {
        return booksRepository.findById(id);
    }

    @Override
    public BooksRequest save(BooksRequest booksRequest) {
        return booksRepository.save(booksRequest);
    }


    @Override
    public BooksRequest update(BooksRequest booksRequest, Integer id) {
        return booksRepository.update(booksRequest, id);
    }


    @Override
    public void deleteById(Integer id) {
        booksRepository.deleteById(id);
    }


}
