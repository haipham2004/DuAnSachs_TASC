//package com.example.notifications_service.event;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class ConsumerEvent {
//
//    @KafkaListener(topics = "Test_Toppics", containerFactory = "kafkaListenerContainerFactory")
//    public void listen(String message) {
//        log.info("Received message: " + message);
//    }
//
//}
