package com.example.books_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MessageExceptionResponse {
    public static final String update_author_errol = "Update author failed";
}
