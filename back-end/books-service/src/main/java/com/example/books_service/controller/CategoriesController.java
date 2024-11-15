package com.example.books_service.controller;

import com.example.books_service.dto.request.CategoriesRequest;
import com.example.books_service.dto.request.PublishersRequest;
import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.service.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    private CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("findAll")
    public ApiResponse<PageResponse<CategoriesResponse>> findAll(@RequestParam(name = "name", defaultValue = "") String name,
                                                                           @RequestParam(defaultValue = "1") int pageNumber,
                                                                           @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<CategoriesResponse>>builder().statusCode(200).message("Fill all author").data(categoriesService.findAll(name, pageNumber, pageSize)).build();
    }

    @PostMapping("save")
    public ApiResponse<CategoriesRequest> save(@Valid @RequestBody CategoriesRequest categoriesRequest){
        return ApiResponse.<CategoriesRequest>builder().message("Save categories success").data(categoriesService.save(categoriesRequest)).build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<CategoriesRequest> update(@PathVariable("id") Integer id, @Valid @RequestBody CategoriesRequest categoriesRequest){
        return ApiResponse.<CategoriesRequest>builder().message("Update categories success").data(categoriesService.update(id,categoriesRequest)).build();
    }
}
