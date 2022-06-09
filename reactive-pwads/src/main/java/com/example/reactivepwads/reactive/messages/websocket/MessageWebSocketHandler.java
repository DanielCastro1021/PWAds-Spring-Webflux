package com.example.reactivepwads.reactive.messages.websocket;

import com.example.reactivepwads.reactive.messages.event.MessageCreatedEvent;
import com.example.reactivepwads.reactive.messages.event.MessageCreatedEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MessageWebSocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;
    Flux<MessageCreatedEvent> publish;

    public MessageWebSocketHandler(ObjectMapper objectMapper, MessageCreatedEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.publish = Flux.create(eventPublisher).share();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
            try {
                return objectMapper.writeValueAsString(evt.getSource());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).map(str -> {
            log.info("WS => sending Ad: " + str);
            return session.textMessage(str);
        });
        return session.send(messageFlux);
    }
}
