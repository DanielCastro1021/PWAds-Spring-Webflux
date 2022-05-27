package com.example.springangularadsapp.components.messages.factory;

import com.example.springangularadsapp.components.messages.model.Message;
import com.example.springangularadsapp.components.messages.model.MessageDto;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    public Message getMessageInstance(Class<? extends MessageDto> type) {
        if (type == MessageDto.class) return new Message();
        else
            return null;
    }
}
