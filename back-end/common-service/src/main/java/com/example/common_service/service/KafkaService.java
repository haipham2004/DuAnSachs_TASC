//package com.example.common_service.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class KafkaService {
//
//    private KafkaTemplate<String,String> kafkaTemplate;
//
//    @Autowired
//    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage(String toppic, String message){
//        kafkaTemplate.send(toppic,message);
//        log.info("kafka send");
//    }
//}
