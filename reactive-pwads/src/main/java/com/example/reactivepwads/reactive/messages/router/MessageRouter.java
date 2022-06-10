package com.example.reactivepwads.reactive.messages.router;

import com.example.reactivepwads.reactive.messages.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class MessageRouter {
    @Bean
    public RouterFunction<ServerResponse> routeMessage(MessageHandler handler) {
        return RouterFunctions
                .route(GET("/api/messages").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/messages/sent").and(accept(MediaType.APPLICATION_JSON)), handler::sent)
                .andRoute(GET("/api/messages/received").and(accept(MediaType.APPLICATION_JSON)), handler::received)
                .andRoute(GET("/api/messages/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST("/api/messages").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(PUT("/api/messages/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/messages/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete)
                .andRoute(GET("/api/messages/received/sse/{username}"), handler::getMessageStream);
    }
}
