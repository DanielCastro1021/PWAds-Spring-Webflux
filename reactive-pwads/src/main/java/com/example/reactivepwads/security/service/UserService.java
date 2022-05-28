package com.example.reactivepwads.security.service;

import com.example.reactivepwads.reactive.users.repository.ReactiveUserRepository;
import com.example.reactivepwads.security.model.Role;
import com.example.reactivepwads.security.model.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * This is just an example, you can load the user from the database from the repository.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final ReactiveUserRepository userRepository;

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Boolean> existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }
}
