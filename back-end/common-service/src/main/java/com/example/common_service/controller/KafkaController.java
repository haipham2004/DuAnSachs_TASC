//package com.example.common_service.controller;
//
//import com.example.common_service.service.KafkaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class KafkaController {
//
//    @Autowired
//    private KafkaService kafkaService;
//
//    @PostMapping("/sendMessage")
//    public void sendMessage(@RequestBody String messsage){
//        kafkaService.sendMessage("Test_topics",messsage);
//    }
//
//}
