package com.example.springangularadsapp.components.messages.notifications;


import com.example.springangularadsapp.components.messages.model.Message;
import com.example.springangularadsapp.firebase.CustomFirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Aspect
public class SendPushNotification {
    private final CustomFirebaseMessagingService firebaseMessagingService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SendPushNotification(CustomFirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }


    @Pointcut("execution(* com.example.springangularadsapp.components.messages.controller.MessageController.save(..))")
    public void saveMessagePointcut() {
    }

    @AfterReturning(value = "saveMessagePointcut()", returning = "responseEntity")
    public void sendPushNotificationToUser(ResponseEntity<?> responseEntity) throws IOException, FirebaseMessagingException {
        EntityModel<Message> entityModel = (EntityModel<Message>) responseEntity.getBody();
        String token = entityModel.getContent().getTo().getFirebaseToken();
        firebaseMessagingService.sendMessageNotification(entityModel, token);
    }
}
