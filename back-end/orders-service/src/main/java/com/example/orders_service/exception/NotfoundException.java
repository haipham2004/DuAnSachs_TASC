package com.example.orders_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotfoundException extends RuntimeException{

    public NotfoundException(String message) {
        super(message);
    }
}
