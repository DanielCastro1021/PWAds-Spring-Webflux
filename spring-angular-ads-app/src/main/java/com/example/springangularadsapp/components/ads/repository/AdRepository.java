package com.example.springangularadsapp.components.ads.repository;

import com.example.springangularadsapp.components.ads.model.ad.Ad;
import com.example.springangularadsapp.security.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdRepository<T extends Ad> extends MongoRepository<T, String> {
    List<T> findByOwner(User owner);
}

