package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.domain.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.domain.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.reactive.users.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BasicAdService extends AdWebfluxService<BasicAd, BasicAdDto> {
    public BasicAdService(ReactiveAdRepository<BasicAd> repository, ReactiveUserRepository userRepository) {
        super(repository, userRepository);
    }


    @Override
    public Flux<BasicAd> myAds() {
        return null;
    }

    @Override
    public Flux<BasicAd> findAll() {
        return null;
    }

    @Override
    public Mono<BasicAd> findById(String id) {
        return null;
    }

    @Override
    public Mono<BasicAd> save(BasicAdDto entity) {
        return null;
    }

    @Override
    public Mono<BasicAd> update(BasicAdDto entity, String id) {
        return null;
    }

    @Override
    public Mono<BasicAd> delete(String id) {
        return null;
    }
}
