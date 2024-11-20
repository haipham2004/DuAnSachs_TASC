package com.example.notifications_service.service.impl;

import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.OrdersResponse;
import com.example.notifications_service.openFeignClient.ApiOrderClient;
import com.example.notifications_service.service.MessageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class MessageServiceImpl implements MessageService {

    private JavaMailSender javaMailSender;

    private SpringTemplateEngine springTemplateEngine;

    private ApiOrderClient apiOrderClient;

    @Autowired
    public MessageServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, ApiOrderClient apiOrderClient) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.apiOrderClient = apiOrderClient;
    }

    @Override
    public void messageOrderSuccess(String to, String subject, String template,Integer idOrder) {
        Context context=new Context();
        ApiResponse<OrdersResponse> orderResponseApi=apiOrderClient.findByIdOrder(idOrder);
        OrdersResponse ordersResponse=orderResponseApi.getData();
        context.setVariable("ordersResponse",ordersResponse);
        String content=springTemplateEngine.process(template,context);
        this.sendEmailSync(to,subject,content,false,true);
    }



    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHTML) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(message,isMultipart, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, isHTML);
            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
