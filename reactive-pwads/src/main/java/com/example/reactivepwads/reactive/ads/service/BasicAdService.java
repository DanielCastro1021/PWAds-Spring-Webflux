package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.security.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BasicAdService extends AdService {
    public BasicAdService(ReactiveAdRepository<Ad> repository, ReactiveUserRepository userRepository, AdMapper adMapper) {
        super(repository, userRepository, adMapper);
    }

    @Override
    public Mono<Ad> save(AdDto entity) {
        return super.getAdMapper().basicAdDtoToBasicAd(entity)
                .flatMap(basicAd -> super.getRepository().save(basicAd).map(ad -> (Ad) ad))
                .switchIfEmpty(Mono.error(new Exception("Could not save Ad: " + entity)));
    }

    @Override
    public Mono<Ad> update(AdDto entity, String id) {
        return super.getRepository().findById(id)
                .flatMap(ad -> super.getAdMapper().basicAdDtoToBasicAd(entity).flatMap(newBasicAd -> {
                    BasicAd updatedAd = (BasicAd) ad;
                    updatedAd.setDescription(newBasicAd.getDescription());
                    updatedAd.setTitle(newBasicAd.getTitle());
                    updatedAd.setImageList(newBasicAd.getImageList());
                    return super.getRepository().save(updatedAd);
                }).map(basicAd -> (Ad) basicAd))
                .switchIfEmpty(Mono.error(new Exception("Could not be update Ad: " + entity)));
    }
}
