package com.example.reactivepwads.reactive.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WebfluxService<T, S> {
    Flux<T> findAll();

    Mono<T> findById(String id);

    Mono<T> save(S entity);

    Mono<T> update(S entity, String id);

    Mono<Void> delete(String id);
}
