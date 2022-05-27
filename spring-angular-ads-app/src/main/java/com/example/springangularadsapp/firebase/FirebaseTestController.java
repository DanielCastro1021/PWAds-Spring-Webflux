package com.example.springangularadsapp.firebase;

import com.example.springangularadsapp.components.messages.model.Message;
import com.example.springangularadsapp.components.messages.assembler.MessageModelAssembler;
import com.example.springangularadsapp.security.authorization.annotation.UserAccess;
import com.example.springangularadsapp.security.models.User;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/firebase")
public class FirebaseTestController {
    /*******************************/
    private final CustomFirebaseMessagingService firebaseService;
    private final MessageModelAssembler assembler;

    public FirebaseTestController(CustomFirebaseMessagingService firebaseService, MessageModelAssembler assembler) {
        this.firebaseService = firebaseService;
        this.assembler = assembler;
    }

    @UserAccess
    @PostMapping("/send-notification/{token}")
    public String sendNotification(@PathVariable String token) throws FirebaseMessagingException, IOException {
        Message msg = new Message();
        msg.setMessage("TestController.sendNotification");
        User user=new User();
        user.setUsername("danielcastro11");
        msg.setFrom(user);
        EntityModel<Message> model = assembler.toModel(msg);
        return firebaseService.sendMessageNotification(model, token);
    }
}
