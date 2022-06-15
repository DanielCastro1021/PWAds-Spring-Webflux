package com.example.reactivepwads.audit.router;

import com.example.reactivepwads.audit.handler.AuditLogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AuditLogRouter {
    @Bean
    public RouterFunction<ServerResponse> routeAuditLog(AuditLogHandler handler) {
        return RouterFunctions
                .route(GET("/api/audit").and(accept(MediaType.APPLICATION_JSON)), handler::findAll);
    }
}
