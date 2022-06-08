package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.reactive.ads.event.AdCreatedEvent;
import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarAdService extends AdWebfluxService<CarAd, CarAdDto> {
    private final ApplicationEventPublisher publisher;

    public CarAdService(AdReactiveRepository<CarAd> repository, UserReactiveRepository userRepository, AdMapper adMapper, ApplicationEventPublisher publisher) {
        super(repository, userRepository, adMapper);
        this.publisher = publisher;
    }

    @Override
    public Flux<CarAd> myAds() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMapMany(user -> super.getRepository().findByOwner(user.getUsername()));
    }

    @Override
    public Flux<CarAd> findAll() {
        return super.getRepository().findAll();
    }

    @Override
    public Mono<CarAd> findById(String id) {
        return super.getRepository().findById(id).switchIfEmpty((Mono.error(new AdNotFoundException(id))));
    }

    @Override
    public Mono<CarAd> save(CarAdDto entity) {
        return super.getAdMapper().carAdDtoToCarAd(entity)
                .flatMap(carAd -> super.getRepository().save(carAd).doOnSuccess(carAd1 -> publisher.publishEvent(new AdCreatedEvent(carAd1))))
                .switchIfEmpty(Mono.error(new Exception("Could not save Ad: " + entity)));
    }

    @Override
    public Mono<CarAd> update(CarAdDto entity, String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMap(user -> super.getRepository().findByIdAndOwner(id, user.getUsername()))
                .flatMap(carAd -> super.getAdMapper().carAdDtoToCarAd(entity).flatMap(newCarAd -> {
                    carAd.setMaker(newCarAd.getMaker());
                    carAd.setModel(newCarAd.getModel());
                    carAd.setYear(newCarAd.getYear());
                    carAd.setImageList(newCarAd.getImageList());
                    return super.getRepository().save(carAd);
                })).switchIfEmpty(Mono.error(new Exception("User does not own the car ad, with this id:" + id + " .")));
    }

    @Override
    public Mono<Void> delete(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMap(user -> super.getRepository().findByIdAndOwner(id, user.getUsername()))
                .switchIfEmpty(Mono.error(new Exception("The user does not own the ad with this id: " + id + ".")))
                .flatMap(ad -> super.getRepository().delete(ad));
    }
}