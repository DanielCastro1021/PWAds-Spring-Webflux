package com.example.reactivepwads.reactive.ads.repository;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;


import com.example.reactivepwads.security.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface ReactiveAdRepository<T extends Ad> extends ReactiveMongoRepository<T, String> {
    Flux<T> findByOwner(User owner);
}
