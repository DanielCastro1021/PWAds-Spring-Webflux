package com.example.springangularadsapp.firebase;


import com.google.firebase.messaging.FirebaseMessagingException;


import com.google.firebase.messaging.Notification;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class CustomFirebaseMessagingService {

    private final CustomFirebaseMessaging firebaseMessaging;

    public CustomFirebaseMessagingService(CustomFirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendMessageNotification(EntityModel<com.example.springangularadsapp.components.messages.model.Message> model, String token) throws FirebaseMessagingException, IOException {
        com.example.springangularadsapp.components.messages.model.Message msg = model.getContent();
        HashMap<String, String> map = new HashMap<>();
        map.put("embedded", model.getContent().toString());
        map.put("_links", model.getLinks().toString());
        Notification notification = Notification
                .builder()
                .setTitle(msg.getFrom().getUsername())
                .setBody(msg.getMessage())
                .build();

        com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(map)
                .build();

        return firebaseMessaging.firebaseMessaging().send(message);
    }

}