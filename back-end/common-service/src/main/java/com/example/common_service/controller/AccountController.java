package com.example.common_service.controller;

import com.example.common_service.model.AccountDto;
import com.example.common_service.model.MessageDto;
import com.example.common_service.model.StaticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("account")
public class AccountController {


    KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    public AccountController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("save")
    public AccountDto create(@RequestBody AccountDto accountDto){
        StaticsDto staticsDto=new StaticsDto("Account"+accountDto.getEmail() +" is Create", new Date());
        MessageDto messageDto= new MessageDto();
        messageDto.setTo(accountDto.getEmail());
        messageDto.setToName(accountDto.getName());
        messageDto.setSubject("WELL COME HAI PHAM YKA");
        messageDto.setContent("AHIHI");

        kafkaTemplate.send("notification",messageDto);
        kafkaTemplate.send("static",staticsDto);

        return accountDto;
    }
}
