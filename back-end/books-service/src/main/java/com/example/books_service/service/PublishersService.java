package com.example.books_service.service;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.request.PublishersRequest;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.dto.response.PublishersResponse;

public interface PublishersService {

    PageResponse<PublishersResponse> findAll(String name, String phone, int pageNum, int pageSize);

    PublishersRequest save(PublishersRequest publishersRequest);

    PublishersRequest update(Integer id, PublishersRequest publishersRequest);

    void delete (Integer id);
}
