package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(Notification notification) {
    String title = "Remember about event ! " + notification.getEventName();
        String content = getContent(notification);
        notification.getEmails().forEach(email -> {
            try {
                sendEmail(email,title,content);
            } catch (MessagingException e) {
                log.error("notification not send "+e.getMessage());
            }
        });
    }

    private String getContent(Notification notification) {
        StringBuilder content = new StringBuilder();
        content.append("Event ");
        content.append(notification.getEventName());
        content.append(" on  "+ notification.getStartDate().toLocalDate());
        content.append(" at "+ notification.getStartDate().getHour());
        return content.toString();
    }

    @Override
    public void sendEmail(EmailDto emailDto) throws MessagingException {
        sendEmail(emailDto.getTo(),emailDto.getTitle(),emailDto.getContent());
    }
    private void sendEmail(String to,String title,String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content,false);
        javaMailSender.send(mimeMessage);
    }
}
