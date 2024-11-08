package com.example.books_service.service.imp;

import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.repository.CategoriesRepository;
import com.example.books_service.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImp implements CategoriesService {

    private CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesServiceImp(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<CategoriesResponse> findAllCategoriesDto() {
        return categoriesRepository.findAllCategoriesDto();
    }
}
