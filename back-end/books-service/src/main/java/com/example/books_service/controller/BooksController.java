package com.example.books_service.controller;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.service.BooksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books")
public class BooksController {

    private BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/findById/{id}")
    public ApiResponse<BooksResponse> findById(@PathVariable("id") Integer id) {
        BooksResponse book = booksService.findById(id);
        return ApiResponse.<BooksResponse>builder()
                .statusCode(200)
                .message("Book found successfully with ID: " + id)
                .data(book)
                .build();
    }


    @PostMapping("save")
    public ApiResponse<BooksRequest> save(@Valid @RequestBody BooksRequest booksRequest) {
        return ApiResponse.<BooksRequest>builder().statusCode(201).message("Create books users succes ").data(booksService.save(booksRequest)).build();
    }


    @PutMapping("update/{id}")
    public ApiResponse<BooksRequest> update(@Valid @RequestBody BooksRequest booksRequest, @PathVariable("id") Integer id) {
        return ApiResponse.<BooksRequest>builder().statusCode(200).message("Update books users succes ").data(booksService.update(booksRequest, id)).build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Integer id, @RequestParam("delete") boolean delete) {
        booksService.deleteById(delete, id);
        return ApiResponse.<Void>builder().statusCode(200).message("Delete book success with ID: " + id).build();
    }

    @GetMapping("findAllBooksPage")
    public ApiResponse<PageResponse<BooksResponse>> findAllBooksPage(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize

    ) {
        return ApiResponse.<PageResponse<BooksResponse>>builder().statusCode(200).message("Page book success").data(booksService.findAllBooksPage(pageNumber, pageSize)).build();
    }


    @GetMapping("findAllBooksPage3")
    public ApiResponse<PageResponse<BooksResponse>> findAllBooksPage3(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "nameAuthor", defaultValue = "") String nameAuthor,
            @RequestParam(name = "namePublisher", defaultValue = "") String namePublisher,
            @RequestParam(name = "nameCategory", defaultValue = "") String nameCategory,
            @RequestParam(defaultValue = "0") double priceMin,
            @RequestParam(defaultValue = "100000000") double priceMax,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", defaultValue = "") String sort) {

        return ApiResponse.<PageResponse<BooksResponse>>builder()
                .statusCode(200)
                .message("Page book success")
                .data(booksService.findBooksPage3(title, nameAuthor, namePublisher, nameCategory,priceMin,priceMax, pageNumber, pageSize, sort))
                .build();
    }

}
