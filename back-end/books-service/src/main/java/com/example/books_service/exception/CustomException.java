package com.example.books_service.exception;

public class CustomException extends RuntimeException{

    private final  MessageExceptionResponse messageExceptionResponse;


    public CustomException(String message, MessageExceptionResponse messageExceptionResponse) {
        super(message);
        this.messageExceptionResponse = messageExceptionResponse;
    }
}
