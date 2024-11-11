package com.example.books_service.exception;

import org.springframework.http.HttpStatusCode;

public class MessageExceptionResponse {

    public static final String name_not_found = "Tên không tồn tại";


    private int statusCode;

    private String message;

    private HttpStatusCode httpStatusCode;

}
