package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.reactive.ads.event.AdCreatedEvent;
import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BasicAdService extends AdWebfluxService<BasicAd, BasicAdDto> {
    private final ApplicationEventPublisher publisher;

    public BasicAdService(AdReactiveRepository<BasicAd> repository, UserReactiveRepository userRepository, AdMapper adMapper, ApplicationEventPublisher publisher) {
        super(repository, userRepository, adMapper);
        this.publisher = publisher;
    }

    @Override
    public Flux<BasicAd> myAds() {
        return ReactiveSecurityContextHolder
                .getContext().map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMapMany(user -> super.getRepository().findByOwner(user.getUsername()));

    }

    @Override
    public Flux<BasicAd> findAll() {
        return super.getRepository().findAll(Example.of(new BasicAd()));
    }

    @Override
    public Mono<BasicAd> findById(String id) {
        return super.getRepository().findById(id).switchIfEmpty((Mono.error(new AdNotFoundException(id))));
    }

    @Override
    public Mono<BasicAd> save(BasicAdDto entity) {
        return super.getAdMapper().basicAdDtoToBasicAd(entity)
                .flatMap(basicAd -> super.getRepository().save(basicAd)
                        .doOnSuccess(basicAd1 -> publisher.publishEvent(new AdCreatedEvent(basicAd1))))
                .switchIfEmpty(Mono.error(new Exception("Could not save BasicAd: " + entity)));
    }

    @Override
    public Mono<BasicAd> update(BasicAdDto entity, String id) {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal())
                .flatMap(userDetails -> getUserRepository().findByUsername(userDetails.toString()))
                .flatMap(user -> super.getRepository().findByIdAndOwner(id, user.getUsername()))
                .flatMap(basicAd -> super.getAdMapper().basicAdDtoToBasicAd(entity).flatMap(newBasicAd -> {
                    basicAd.setDescription(newBasicAd.getDescription());
                    basicAd.setTitle(newBasicAd.getTitle());
                    basicAd.setImageList(newBasicAd.getImageList());
                    return super.getRepository().save(basicAd);
                })).switchIfEmpty(Mono.error(new Exception("User does not own the basic ad, with this id:" + id + " .")));
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
