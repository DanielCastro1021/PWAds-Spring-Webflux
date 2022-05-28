package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.domain.ads.model.car_ad.CarAd;
import com.example.reactivepwads.domain.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.reactive.users.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarAdService extends AdWebfluxService<CarAd, CarAdDto> {
    public CarAdService(ReactiveAdRepository<CarAd> repository, ReactiveUserRepository userRepository) {
        super(repository, userRepository);
    }

    @Override
    public Flux<CarAd> myAds() {
        return null;
    }

    @Override
    public Flux<CarAd> findAll() {
        return null;
    }

    @Override
    public Mono<CarAd> findById(String id) {
        return null;
    }

    @Override
    public Mono<CarAd> save(CarAdDto entity) {
        return null;
    }

    @Override
    public Mono<CarAd> update(CarAdDto entity, String id) {
        return null;
    }

    @Override
    public Mono<CarAd> delete(String id) {
        return null;
    }
}