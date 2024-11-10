package com.example.users_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testnhe")
public class TestController {

    @GetMapping("wellcome")
    public String wellCOme(){
        return "Xin chào hải phạm";
    }
}
