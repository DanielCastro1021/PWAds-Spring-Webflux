package com.example.reactivepwads.config.websocket.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }
}
