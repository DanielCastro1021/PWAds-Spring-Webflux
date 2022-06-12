package com.example.reactivepwads.audit.repository;

import com.example.reactivepwads.audit.model.AuditLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuditLogReactiveRepository extends ReactiveMongoRepository<AuditLog, String> {
}
