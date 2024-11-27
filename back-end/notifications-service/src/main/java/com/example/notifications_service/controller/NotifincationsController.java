package com.example.notifications_service.controller;

import com.example.notifications_service.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotifincationsController {


    @Value("${notifications.events.topic.name}")
    private String notificationsTopic;


    private  KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public NotifincationsController( KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // API nhận yêu cầu gửi email
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody MessageDto messageDto) {
        // Phát sự kiện qua Kafka
        kafkaTemplate.send(notificationsTopic, messageDto);

        return "Yêu cầu gửi email đã được gửi tới Kafka rồi nhé HaiPham YKA";
    }
}
