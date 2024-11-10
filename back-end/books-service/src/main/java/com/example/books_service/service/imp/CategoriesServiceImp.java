package com.example.books_service.service.imp;

import com.example.books_service.dto.response.CategoriesResponse;
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
    public List<CategoriesResponse> findAllCategoriesDto() {
        return categoriesServiceRepository.findAllCategoriesDto();
    }
}
