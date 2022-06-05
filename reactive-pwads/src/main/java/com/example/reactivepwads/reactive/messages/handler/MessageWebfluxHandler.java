package com.example.reactivepwads.reactive.messages.handler;

import com.example.reactivepwads.reactive.util.WebfluxHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface MessageWebfluxHandler extends WebfluxHandler {
    public Mono<ServerResponse> sent(ServerRequest request);

    public Mono<ServerResponse> received(ServerRequest request);
}
