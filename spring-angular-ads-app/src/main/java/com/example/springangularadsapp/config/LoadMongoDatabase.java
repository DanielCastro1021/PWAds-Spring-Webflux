package com.example.springangularadsapp.config;

import com.example.springangularadsapp.security.models.ERole;
import com.example.springangularadsapp.security.models.Role;
import com.example.springangularadsapp.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadMongoDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadMongoDatabase.class);

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName(ERole.ROLE_USER)) {
                roleRepository.save(new Role(ERole.ROLE_USER));
                log.info("LoadDatabaseRoles => added ERole.ROLE_USER");
            }
            if (!roleRepository.existsByName(ERole.ROLE_MODERATOR)) {
                roleRepository.save(new Role(ERole.ROLE_MODERATOR));
                log.info("LoadDatabaseRoles => added ERole.ROLE_MODERATOR");
            }

            if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
                log.info("LoadDatabaseRoles => added ERole.ROLE_ADMIN");
            }
            roleRepository.findAll().forEach(role -> {
                log.info("Preloaded " + role.getName());
            });
        };
    }
}
