package com.example.notifications_service.service.impl;

import com.example.notifications_service.dto.MessageDto;
import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.OrdersResponse;
import com.example.notifications_service.openFeignClient.ApiOrderClient;
import com.example.notifications_service.service.EmailServiceNotification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailServiceNotificationImpl implements EmailServiceNotification {

    @Value("${spring.mail.username}")
    private String from;

    private JavaMailSender javaMailSender;
    private SpringTemplateEngine springTemplateEngine;
    private ApiOrderClient apiOrderClient;

    @Autowired
    public EmailServiceNotificationImpl(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, ApiOrderClient apiOrderClient) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.apiOrderClient = apiOrderClient;
    }

    @Override
    public void sendEmail(MessageDto messageDto) {
        log.info("Send email notification");
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            Context context = new Context();
            ApiResponse<OrdersResponse> orderResponseApi = apiOrderClient.findByIdOrder(messageDto.getIdOrder());
            OrdersResponse ordersResponse = orderResponseApi.getData();
            context.setVariable("ordersResponse", ordersResponse);
            context.setVariable("name", messageDto.getToName());
            context.setVariable("content", messageDto.getContent());

            String html = springTemplateEngine.process("OrderSuccess.html", context);

            mimeMessageHelper.setTo(messageDto.getTo());
            mimeMessageHelper.setSubject(messageDto.getToSubject());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setFrom(from);

            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }
}
