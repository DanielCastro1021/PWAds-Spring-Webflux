package com.example.reactivepwads.reactive.ads.service;

import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.reactive.ads.util.AdWebfluxService;
import com.example.reactivepwads.security.repository.ReactiveUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarAdService extends AdService {
    public CarAdService(ReactiveAdRepository<Ad> repository, ReactiveUserRepository userRepository, AdMapper adMapper) {
        super(repository, userRepository, adMapper);
    }

    @Override
    public Mono<Ad> save(AdDto entity) {
        return super.getAdMapper().carAdDtoToCarAd(entity)
                .flatMap(carAd -> super.getRepository().save(carAd).map(ad -> (Ad) ad))
                .switchIfEmpty(Mono.error(new Exception("Could not save Ad: " + entity)));
    }

    @Override
    public Mono<Ad> update(AdDto entity, String id) {
        return super.getRepository().findById(id)
                .flatMap(ad -> super.getAdMapper().carAdDtoToCarAd(entity).flatMap(newCarAd -> {
                    CarAd updatedAd = (CarAd) ad;
                    updatedAd.setMaker(newCarAd.getMaker());
                    updatedAd.setModel(newCarAd.getModel());
                    updatedAd.setYear(newCarAd.getYear());
                    updatedAd.setImageList(newCarAd.getImageList());
                    return super.getRepository().save(updatedAd);
                }).map(basicAd -> (Ad) basicAd))
                .switchIfEmpty(Mono.error(new Exception("Could not be update Ad: " + entity)));
    }
}