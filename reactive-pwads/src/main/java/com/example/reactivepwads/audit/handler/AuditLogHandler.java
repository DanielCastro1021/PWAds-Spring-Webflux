package com.example.reactivepwads.audit.handler;

import com.example.reactivepwads.audit.service.AuditLogService;
import com.example.reactivepwads.reactive.util.WebfluxHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class AuditLogHandler implements WebfluxHandler {
    private final AuditLogService service;

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), AuditLogService.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        return null;
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        return null;
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return null;
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        return null;
    }
}
