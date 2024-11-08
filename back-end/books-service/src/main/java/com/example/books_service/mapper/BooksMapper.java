package com.example.books_service.mapper;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.entity.Books;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface BooksMapper {

    BooksResponse mapToBooksResponse(Books books);

    Books mapToBooks(BooksRequest booksRequest);

    BooksRequest mapToBooksRequest(Books books);
}
