package com.example.notifications_service.controller;

import com.example.notifications_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("sendEmail")
    public String sendEmail(){
        emailService.sendEmailSyncTempale("bophamnb2004@gmail.com","Xin Chào Khách Hàng","test");
        return "Send email success";
    }
}
