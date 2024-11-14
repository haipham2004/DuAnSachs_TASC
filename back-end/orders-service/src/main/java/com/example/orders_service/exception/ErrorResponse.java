package com.example.orders_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private final int status;

    private final String message;

    private final long timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp=System.currentTimeMillis();
    }
}
