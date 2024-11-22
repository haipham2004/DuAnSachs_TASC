package com.example.users_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("check")
public class TestController {

    @GetMapping("wellCome")
    public String wellCOme(){
        return "Xin chào hải phạm";
    }

    @GetMapping("goodBye")
    public String goodBye(){
        return "Tạm biệt hải phạm";
    }
}
