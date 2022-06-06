package com.example.reactivepwads.reactive.messages.repository;

import com.example.reactivepwads.reactive.messages.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MessageReactiveRepository extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findByTo(String to);

    Flux<Message> findByFrom(String from);

}
