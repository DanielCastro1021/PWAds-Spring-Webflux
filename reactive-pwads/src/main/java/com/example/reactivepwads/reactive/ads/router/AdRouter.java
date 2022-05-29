package com.example.reactivepwads.reactive.ads.router;

import com.example.reactivepwads.reactive.ads.handler.AdHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AdRouter {

    @Bean
    public RouterFunction<ServerResponse> route(AdHandler handler) {
        return RouterFunctions
                .route(GET("/api/ads").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(GET("/api/ads/myAds").and(accept(MediaType.APPLICATION_JSON)), handler::myAds)
                .andRoute(POST("/api/ads").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(PUT("/api/ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);

    }
}