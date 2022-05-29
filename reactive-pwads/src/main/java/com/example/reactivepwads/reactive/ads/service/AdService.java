package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.security.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AdService extends AdWebfluxService<Ad, AdDto> {
    public AdService(ReactiveAdRepository<Ad> repository, ReactiveUserRepository userRepository, AdMapper adMapper) {
        super(repository, userRepository, adMapper);
    }

    @Override
    public Flux<Ad> myAds(ServerRequest request) {
        return super.getUserRepository()
                .findByUsername(request.principal().toString())
                .flatMapMany(user -> super.getRepository().findByOwner(user));
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
    public Mono<Ad> delete(String id) {
        return super.getRepository().findById(id)
                .flatMap(ad -> Mono.just(super.getRepository().delete(ad)).then(Mono.just(ad)))
                .switchIfEmpty(Mono.error(new AdNotFoundException(id)));
    }


    @Override
    public Mono<Ad> save(AdDto entity) {
        return null;
    }

    @Override
    public Mono<Ad> update(AdDto entity, String id) {
        return null;
    }
}
