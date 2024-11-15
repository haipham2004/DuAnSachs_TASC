package com.example.books_service.service.imp;

import com.example.books_service.dto.request.CategoriesRequest;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.repository.CategoriesServiceRepository;
import com.example.books_service.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImp implements CategoriesService {

    private CategoriesServiceRepository categoriesServiceRepository;

    @Autowired
    public CategoriesServiceImp(CategoriesServiceRepository categoriesServiceRepository) {
        this.categoriesServiceRepository = categoriesServiceRepository;
    }

    @Override
    public PageResponse<CategoriesResponse> findAll(String name, int pageNum, int pageSize) {
        return categoriesServiceRepository.findAll(name,pageNum,pageSize);
    }

    @Override
    public CategoriesRequest save(CategoriesRequest categoriesRequest) {
        return categoriesServiceRepository.save(categoriesRequest);
    }

    @Override
    public CategoriesRequest update(Integer id, CategoriesRequest categoriesRequest) {
        return categoriesServiceRepository.update(id, categoriesRequest);
    }

    @Override
    public void delete(Integer id) {

    }
}
