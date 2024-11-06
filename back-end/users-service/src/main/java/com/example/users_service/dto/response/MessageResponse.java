package com.example.users_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {
    private String message;

    private Integer id;

    public MessageResponse(String message, Integer id) {
        this.message = message;
        this.id = id;
    }
}