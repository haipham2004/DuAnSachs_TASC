package com.example.notifications_service.controller;

import com.example.notifications_service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notifications")
public class SendEmailController {

    private MessageService messageService;

    @Autowired
    public SendEmailController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("sendEmailOrserSuccess")
    public String sendEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam(name = "template", defaultValue = "OrderSuccess") String template,
            @RequestParam("idOrder") Integer idOrder

    ) {
        messageService.messageOrderSuccess(to, subject, template, idOrder);
        return "Send email success";
    }
}
