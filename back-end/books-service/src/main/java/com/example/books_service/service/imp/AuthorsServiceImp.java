package com.example.books_service.service.imp;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.repository.AuthorRepository;
import com.example.books_service.service.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsServiceImp implements AuthorsService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorsServiceImp(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorsResponse> findAllAuthorsDto() {
        return authorRepository.findAllAuthorsDto();
    }
}
