package com.example.reactivepwads.reactive.audit.repository;

import com.example.reactivepwads.reactive.audit.model.AuditLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuditLogReactiveRepository extends ReactiveMongoRepository<AuditLog, String> {
}
