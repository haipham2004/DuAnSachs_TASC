package com.example.notifications_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@EnableAutoConfiguration
public class EmailService {

    private JavaMailSender javaMailSender;

    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

//    public void sendEmail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("haipnph39701@fpt.edu.vn");  // Địa chỉ gửi email
//        message.setTo("hoa24102004@gmail.com");                        // Địa chỉ người nhận
//        message.setSubject("Hải Phạm YKA");
//        message.setText("Spring boot java: Send email from haipnph39701@fpt.edu.vn to hoa24102004@gmail.com success");
//
//        javaMailSender.send(message);
//    }


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

    public void sendEmailSyncTempale(String to, String subject,  String tempalte){
        Context context=new Context();
        context.setVariable("name",to);
        String content=springTemplateEngine.process(tempalte,context);
        this.sendEmailSync(to,subject,content,false,true);
    }
}
