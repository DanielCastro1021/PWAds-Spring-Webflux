package com.example.reactivepwads.security.repository;


import com.example.reactivepwads.security.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);
}
