package com.example.books_service.controller;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.service.AuthorsService;
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

@RestController
@RequestMapping("authors")
public class AuthorsController {

    private AuthorsService authorsService;

    @Autowired
    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping("findAll")
    public ApiResponse<PageResponse<AuthorsResponse>> findAll(@RequestParam(name = "name", defaultValue = "") String name,
                                                                 @RequestParam(name = "phone", defaultValue = "") String phone,
                                                                 @RequestParam(defaultValue = "1") int pageNumber,
                                                                 @RequestParam(defaultValue = "30") int pageSize){
        return ApiResponse.<PageResponse<AuthorsResponse>>builder().statusCode(200).message("Fill all author").data(authorsService.findAllAuthorsDto(name, phone, pageNumber, pageSize)).build();
    }

    @PostMapping("save")
    public ApiResponse<AuthorsRequest> save(@Valid @RequestBody AuthorsRequest authorsRequest){
        return ApiResponse.<AuthorsRequest>builder().message("Save Author success").data(authorsService.save(authorsRequest)).build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<AuthorsRequest> update(@PathVariable("id") Integer id, @Valid @RequestBody AuthorsRequest authorsRequest){
        return ApiResponse.<AuthorsRequest>builder().message("Update Author success").data(authorsService.update(id,authorsRequest)).build();
    }
}
