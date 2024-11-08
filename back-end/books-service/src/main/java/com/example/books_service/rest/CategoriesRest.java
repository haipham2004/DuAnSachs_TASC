package com.example.books_service.rest;

import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.service.AuthorsService;
import com.example.books_service.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesRest {

    private CategoriesService categoriesService;

    @Autowired
    public CategoriesRest(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("findAllCategoriesDto")
    public ApiResponse<List<CategoriesResponse>> findAllAuthorsDto(){
        return ApiResponse.<List<CategoriesResponse>>builder().statusCode(200).message("Fill all author").data(categoriesService.findAllCategoriesDto()).build();
    }
}
