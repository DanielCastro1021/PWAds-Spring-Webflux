package com.example.reactivepwads.reactive.messages.service;

import com.example.reactivepwads.reactive.messages.model.Message;
import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.reactive.messages.repository.MessageReactiveRepository;
import com.example.reactivepwads.reactive.messages.util.PersonalMessageService;
import com.example.reactivepwads.reactive.util.WebfluxService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MessageService implements WebfluxService<Message, MessageDto>, PersonalMessageService {
    private final MessageReactiveRepository messageReactiveRepository;

    @Override
    public Flux<Message> sent(ServerRequest request) {
        return null;
    }

    @Override
    public Flux<Message> received(ServerRequest request) {
        return null;
    }

    @Override
    public Flux<Message> findAll() {
        return null;
    }

    @Override
    public Mono<Message> findById(String id) {
        return null;
    }

    @Override
    public Mono<Message> save(MessageDto entity) {
        return null;
    }

    @Override
    public Mono<Message> update(MessageDto entity, String id) {
        return null;
    }

    @Override
    public Mono<Message> delete(String id) {
        return null;
    }
}
