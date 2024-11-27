package com.example.notifications_service.controller;

import com.example.notifications_service.dto.MessageDto;
import com.example.notifications_service.service.impl.EmailServiceNotificationImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaEmailListener {

    private final EmailServiceNotificationImpl emailServiceNotification;

    @Autowired
    public KafkaEmailListener(EmailServiceNotificationImpl emailServiceNotification) {
        this.emailServiceNotification = emailServiceNotification;
    }

//    @RetryableTopic(attempts = "2", dltTopicSuffix = "-dlt",
//            backoff = @Backoff(delay = 2_000, multiplier = 2),
//            autoCreateTopics = "true",
//            dltStrategy = DltStrategy.FAIL_ON_ERROR,
//            include = {RuntimeException.class, RetriableException.class}
//    )
    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void handleEmailEvent(MessageDto messageDto) {

        if (messageDto != null && messageDto.getTo() != null && messageDto.getContent() != null) {
            emailServiceNotification.sendEmail(messageDto);
            log.info("Đã nhận được tin nhắn từ chủ đề: {}", messageDto);
        } else {
            log.error("Thông tin tin nhắn không hợp lệ: {}", messageDto);
        }

    }

//    @DltHandler
//        public void dltListen(MessageDto messageDto) {
//        log.error("Dlt Đã xảy ra lỗi khi gửi email: " + messageDto);
//    }

//    @KafkaListener(id = "dltGroup", topics = "notification-events-dlt")
//    public void dltListen(MessageDto messageDto) {
//        log.error("Đã xảy ra lỗi khi gửi email: " + messageDto);
//    }
}

