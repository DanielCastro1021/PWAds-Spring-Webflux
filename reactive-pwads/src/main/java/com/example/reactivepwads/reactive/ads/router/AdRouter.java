package com.example.reactivepwads.reactive.ads.router;

import com.example.reactivepwads.reactive.ads.handler.AdHandler;
import com.example.reactivepwads.reactive.ads.handler.BasicAdHandler;
import com.example.reactivepwads.reactive.ads.handler.CarAdHandler;
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
    public RouterFunction<ServerResponse> routeAd(AdHandler handler) {
        return RouterFunctions
                .route(GET("/api/ads").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/ads/myAds").and(accept(MediaType.APPLICATION_JSON)), handler::myAds)
                .andRoute(GET("/api/ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(DELETE("/api/ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);

    }

    @Bean
    public RouterFunction<ServerResponse> routeBasicAd(BasicAdHandler handler) {
        return RouterFunctions
                .route(GET("/api/basic-ads").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/basic-ads/myAds").and(accept(MediaType.APPLICATION_JSON)), handler::myAds)
                .andRoute(GET("/api/basic-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST("/api/basic-ads").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(PUT("/api/basic-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/basic-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);

    }

    @Bean
    public RouterFunction<ServerResponse> routeCarAd(CarAdHandler handler) {
        return RouterFunctions
                .route(GET("/api/car-ads").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/car-ads/myAds").and(accept(MediaType.APPLICATION_JSON)), handler::myAds)
                .andRoute(GET("/api/car-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST("/api/car-ads").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(PUT("/api/car-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/car-ads/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);
    }
}