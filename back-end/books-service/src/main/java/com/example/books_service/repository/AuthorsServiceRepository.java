package com.example.books_service.repository;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.PageResponse;

public interface AuthorsServiceRepository {

     PageResponse<AuthorsResponse> findAllAuthorsDto( String name, String phone, int pageNum, int pageSize);

    AuthorsRequest save(AuthorsRequest authorsRequest);

    AuthorsRequest update(Integer id, AuthorsRequest authorsRequest);

    void delete (Integer id);
}
