package com.example.reactivepwads.reactive.util;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface WebfluxHandler {
    public Mono<ServerResponse> findAll(ServerRequest request);

    public Mono<ServerResponse> findById(ServerRequest request);

    public Mono<ServerResponse> save(ServerRequest request);

    public Mono<ServerResponse> update(ServerRequest request);

    public Mono<ServerResponse> delete(ServerRequest request);
}
