package com.example.books_service.service;

import com.example.books_service.dto.request.CategoriesRequest;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.dto.response.PageResponse;

public interface CategoriesService {

    PageResponse<CategoriesResponse> findAll(String name, int pageNum, int pageSize);

    CategoriesRequest save(CategoriesRequest categoriesRequest);

    CategoriesRequest update(Integer id, CategoriesRequest categoriesRequest);

    void delete (Integer id);
}
