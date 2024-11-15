package com.example.books_service.controller;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.request.PublishersRequest;
import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.service.PublishersService;
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
@RequestMapping("publisher")
public class PublishersController {

    private PublishersService publishersService;

    @Autowired
    public PublishersController(PublishersService publishersService) {
        this.publishersService = publishersService;
    }

    @GetMapping("findAll")
    public ApiResponse<PageResponse<PublishersResponse>> findAll(@RequestParam(name = "name", defaultValue = "") String name,
                                                                        @RequestParam(name = "phone", defaultValue = "") String phone,
                                                                        @RequestParam(defaultValue = "1") int pageNumber,
                                                                        @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<PublishersResponse>>builder().statusCode(200).message("Fill all author").data(publishersService.findAll(name, phone, pageNumber, pageSize)).build();
    }

    @PostMapping("save")
    public ApiResponse<PublishersRequest> save(@Valid @RequestBody PublishersRequest publishersRequest){
        return ApiResponse.<PublishersRequest>builder().message("Save Publisher success").data(publishersService.save(publishersRequest)).build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<PublishersRequest> update(@PathVariable("id") Integer id, @Valid @RequestBody PublishersRequest publishersRequest){
        return ApiResponse.<PublishersRequest>builder().message("Update Publisher success").data(publishersService.update(id,publishersRequest)).build();
    }
}
