package com.example.reactivepwads.reactive.messages.event;

import com.example.reactivepwads.reactive.messages.model.Message;
import org.springframework.context.ApplicationEvent;

public class MessageCreatedEvent extends ApplicationEvent {
    public MessageCreatedEvent(Message source) {
        super(source);
    }
}
