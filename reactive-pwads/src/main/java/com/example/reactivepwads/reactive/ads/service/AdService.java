package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AdService extends AdWebfluxService<Ad, AdDto> {
    public AdService(AdReactiveRepository<Ad> repository, UserReactiveRepository userRepository, AdMapper adMapper) {
        super(repository, userRepository, adMapper);
    }

    @Override
    public Flux<Ad> myAds() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMapMany(user -> super.getRepository().findByOwner(user.getUsername()));
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
