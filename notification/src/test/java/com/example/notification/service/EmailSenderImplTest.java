package com.example.notification.service;

import com.example.notification.model.dto.EmailDto;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EmailSenderImplTest {
    @Autowired
     EmailSender emailSender;

    @Test
    void sendEmail() throws MessagingException {
        EmailDto emailDto = EmailDto.builder()
                .to("tomaszhamerla15@gmail.com")
                .title("example")
                .content("example")
                .build();
        emailSender.sendEmail(emailDto);
    }
}