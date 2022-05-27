package com.example.springangularadsapp.security.repository;

import java.util.Optional;

import com.example.springangularadsapp.security.models.ERole;
import com.example.springangularadsapp.security.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);

    Boolean existsByName(ERole name);
}
