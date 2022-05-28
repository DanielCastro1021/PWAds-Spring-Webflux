package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.domain.ads.model.ad.Ad;
import com.example.reactivepwads.domain.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.reactive.users.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AdService extends AdWebfluxService<Ad, AdDto> {
    public AdService(ReactiveAdRepository<Ad> repository, ReactiveUserRepository userRepository) {
        super(repository, userRepository);
    }

    @Override
    public Flux<Ad> myAds() {
        return null;
    }

    @Override
    public Flux<Ad> findAll() {
        return super.getRepository().findAll();
    }

    @Override
    public Mono<Ad> findById(String id) {
        return super.getRepository().findById(id).switchIfEmpty((Mono.error(new AdNotFoundException(id))));
    }

    @Override
    public Mono<Ad> save(AdDto entity) {
        return null;
    }

    @Override
    public Mono<Ad> update(AdDto entity, String id) {
        return null;
    }

    @Override
    public Mono<Ad> delete(String id) {
        return null;
    }
}
