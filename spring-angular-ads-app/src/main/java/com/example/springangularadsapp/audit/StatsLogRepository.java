package com.example.springangularadsapp.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatsLogRepository extends MongoRepository<StatsLog, String> {
}
