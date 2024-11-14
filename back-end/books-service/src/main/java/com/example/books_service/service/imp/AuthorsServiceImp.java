package com.example.books_service.service.imp;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.repository.AuthorsServiceRepository;
import com.example.books_service.service.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsServiceImp implements AuthorsService {

    private AuthorsServiceRepository authorsServiceRepository;

    @Autowired
    public AuthorsServiceImp(AuthorsServiceRepository authorsServiceRepository) {
        this.authorsServiceRepository = authorsServiceRepository;
    }

    @Override
    public List<AuthorsResponse> findAllAuthorsDto() {
        return authorsServiceRepository.findAllAuthorsDto();
    }

    @Override
    public AuthorsRequest save(AuthorsRequest authorsRequest) {
        return authorsServiceRepository.save(authorsRequest);
    }

    @Override
    public AuthorsRequest update(Integer id, AuthorsRequest authorsRequest) {
        return authorsServiceRepository.update(id, authorsRequest);
    }

    @Override
    public void delete(Integer id) {

    }
}
