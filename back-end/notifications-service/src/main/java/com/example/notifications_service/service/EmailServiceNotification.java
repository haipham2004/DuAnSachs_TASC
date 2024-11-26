package com.example.notifications_service.service;

import com.example.notifications_service.dto.MessageDto;

public interface EmailServiceNotification {

    void sendEmail(MessageDto messageDto);
}
