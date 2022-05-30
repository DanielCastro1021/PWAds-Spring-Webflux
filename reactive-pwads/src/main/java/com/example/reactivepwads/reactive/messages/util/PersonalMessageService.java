package com.example.reactivepwads.reactive.messages.util;

import com.example.reactivepwads.reactive.messages.model.Message;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

public interface PersonalMessageService {
    Flux<Message> sent(ServerRequest request);

    Flux<Message> received(ServerRequest request);
}
