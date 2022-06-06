package com.example.reactivepwads.reactive.messages.factory;

import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.reactive.messages.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    public Message getMessageInstance(Class<? extends MessageDto> type) {
        if (type == MessageDto.class) return new Message();
        else
            return null;
    }
}
