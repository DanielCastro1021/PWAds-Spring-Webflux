package com.example.reactivepwads.audit.service;

import com.example.reactivepwads.audit.model.AuditLog;
import com.example.reactivepwads.audit.repository.AuditLogReactiveRepository;
import com.example.reactivepwads.reactive.util.WebfluxService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuditLogService implements WebfluxService<AuditLog, Object> {

    private final AuditLogReactiveRepository repository;

    @Override
    public Flux<AuditLog> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<AuditLog> findById(String id) {
        return null;
    }

    @Override
    public Mono<AuditLog> save(Object entity) {
        return null;
    }

    @Override
    public Mono<AuditLog> update(Object entity, String id) {
        return null;
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }
}
