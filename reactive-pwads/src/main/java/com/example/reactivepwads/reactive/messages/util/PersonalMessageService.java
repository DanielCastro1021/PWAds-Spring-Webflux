package com.example.reactivepwads.reactive.messages.util;

import com.example.reactivepwads.reactive.messages.model.Message;
import reactor.core.publisher.Flux;

public interface PersonalMessageService {
    Flux<Message> sent();

    Flux<Message> received();
}
