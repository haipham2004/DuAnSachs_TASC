package com.example.books_service.controller;

import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.service.PublishersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("publisher")
public class PublishersController {

    private PublishersService publishersService;

    @Autowired
    public PublishersController(PublishersService publishersService) {
        this.publishersService = publishersService;
    }

    @GetMapping("findAllPublisherDto")
    public ApiResponse<List<PublishersResponse>> findAllAuthorsDto(){
        return ApiResponse.<List<PublishersResponse>>builder().statusCode(200).message("Fill all publissher").data(publishersService.findAllPublisherDto()).build();
    }
}
