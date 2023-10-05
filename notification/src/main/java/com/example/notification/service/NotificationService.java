package com.example.notification.service;

import com.example.notification.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @RabbitListener(queues = "test")
   public void getNotification(Notification notification){
      log.info(notification.toString());
    }
}
