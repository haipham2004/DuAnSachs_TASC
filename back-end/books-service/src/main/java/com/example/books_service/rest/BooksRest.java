package com.example.books_service.rest;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.request.BooksRequest2;
import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.BooksResponse2;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.service.BooksService;
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

import java.util.List;

@RestController
@RequestMapping("books")
public class BooksRest {

    private BooksService booksService;

    @Autowired
    public BooksRest(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/findByIdBooks/{id}")
    public ApiResponse<BooksResponse> findById(@PathVariable("id") Integer id) {
        BooksResponse book = booksService.findById(id);
        return ApiResponse.<BooksResponse>builder()
                .statusCode(200)
                .message("Book found successfully with ID: " + id)
                .data(book)
                .build();
    }


    @GetMapping("findAllBooksDto")
    public ApiResponse<List<BooksResponse>> findAllBooksDto() {
      return   ApiResponse.<List<BooksResponse>>builder().data(booksService.findAllBooksDto()).build();
    }

    @PostMapping("saveBooks")
    public ApiResponse<BooksRequest> saveBooks(@RequestBody BooksRequest booksRequest){
        return ApiResponse.<BooksRequest>builder().statusCode(201).message("Create books users succes ").data(booksService.save(booksRequest)).build();
    }

    @PostMapping("saveBooks2")
    public ApiResponse<BooksRequest2> saveBooks(@RequestBody BooksRequest2 booksRequest2){
        return ApiResponse.<BooksRequest2>builder().statusCode(201).message("Create books users succes ").data(booksService.save2(booksRequest2)).build();
    }


    @PutMapping("updateBooks/{id}")
    public ApiResponse<BooksRequest> updateBooks(@RequestBody BooksRequest booksRequest, @PathVariable("id") Integer id){
        return ApiResponse.<BooksRequest>builder().statusCode(200).message("Update books users succes ").data(booksService.update(booksRequest,id)).build();
    }

    @DeleteMapping("deleteBooks/{id}")
    public ApiResponse<Void> deleteBooks(@PathVariable("id") Integer id){
        booksService.deleteById(id);
        return ApiResponse.<Void>builder().statusCode(200).message("Delete book success with ID: "+id).build();
    }

    @GetMapping("findAllBooksPage")
    public ApiResponse<PageResponse<BooksResponse>> findAllBooksPage(
            @RequestParam( defaultValue = "1") int pageNumber,
            @RequestParam( defaultValue = "5") int pageSize

    ) {
        return   ApiResponse.<PageResponse<BooksResponse>>builder().statusCode(200).message("Page book success").data(booksService.findAllBooksPage(pageNumber,pageSize)).build();
    }

    @GetMapping("findAllBooksPage2")
    public ApiResponse<PageResponse<BooksResponse2>> findAllBooksPage2(
            @RequestParam( defaultValue = "1") int pageNumber,
            @RequestParam( defaultValue = "5") int pageSize

    ) {
        return   ApiResponse.<PageResponse<BooksResponse2>>builder().statusCode(200).message("Page book success").data(booksService.findAllBooksPage2(pageNumber,pageSize)).build();
    }
}
