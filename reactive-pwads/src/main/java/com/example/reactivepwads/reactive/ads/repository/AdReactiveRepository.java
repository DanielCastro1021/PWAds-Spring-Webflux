package com.example.reactivepwads.reactive.ads.repository;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface AdReactiveRepository<T extends Ad> extends ReactiveMongoRepository<T, String> {
    Flux<T> findByOwner(String owner);

    Mono<T> findByIdAndOwner(String id, String owner);
}
