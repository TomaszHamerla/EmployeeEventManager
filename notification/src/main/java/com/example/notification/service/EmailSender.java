package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.dto.EmailDto;
import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendEmail(Notification notification);
    void sendEmail(EmailDto emailDto) throws MessagingException;
}
