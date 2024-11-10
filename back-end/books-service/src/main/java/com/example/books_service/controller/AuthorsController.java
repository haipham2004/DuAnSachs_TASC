package com.example.books_service.controller;

import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.service.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorsController {

    private AuthorsService authorsService;

    @Autowired
    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping("findAllAuthorsDto")
    public ApiResponse<List<AuthorsResponse>> findAllAuthorsDto(){
        return ApiResponse.<List<AuthorsResponse>>builder().statusCode(200).message("Fill all author").data(authorsService.findAllAuthorsDto()).build();
    }
}