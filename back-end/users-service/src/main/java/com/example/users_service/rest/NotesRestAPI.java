package com.example.users_service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotesRestAPI {
    @GetMapping("/api/notes/wellcome")
    public String wellcome(){
        return "Xin chào nguyen hang nhe";
    }
}
