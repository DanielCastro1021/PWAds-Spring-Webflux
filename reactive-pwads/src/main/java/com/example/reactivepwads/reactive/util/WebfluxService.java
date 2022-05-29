package com.example.reactivepwads.reactive.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WebfluxService<T, S> {
    Flux<? extends T> findAll();

    Mono<? extends T> findById(String id);

    Mono<? extends T> save(S entity);

    Mono<? extends T> update(S entity, String id);

    Mono<? extends T> delete(String id);
}
