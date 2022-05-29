package com.example.reactivepwads.reactive.ads.handler;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.service.AdService;
import com.example.reactivepwads.reactive.ads.service.BasicAdService;
import com.example.reactivepwads.reactive.ads.service.CarAdService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class AdHandler implements AdWebfluxHandler {
    private final AdService adService;
    private final CarAdService carAdService;
    private final BasicAdService basicAdService;

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> myAds(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(adService.myAds(request), Ad.class);
    }

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(adService.findAll(), Ad.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(adService.findById(id), Ad.class);
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<AdDto> dto = request.bodyToMono(AdDto.class);
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(dto.flatMap(adDto -> {
                    if (adDto.getClass() == CarAdDto.class) {
                        return carAdService.save(adDto);
                    } else if (adDto.getClass() == BasicAdDto.class) {
                        return basicAdService.save(adDto);
                    } else return Mono.error(new Exception("Something went wrong in AdService.save method"));
                }), Ad.class));
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> update(ServerRequest request) {
        final Mono<AdDto> dto = request.bodyToMono(AdDto.class);
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(dto.flatMap(adDto -> {
                    if (adDto.getClass() == CarAdDto.class) {
                        return carAdService.update(adDto, id);
                    } else if (adDto.getClass() == BasicAdDto.class) {
                        return basicAdService.update(adDto, id);
                    } else return Mono.error(new Exception("Something went wrong in AdService.update method"));
                }), Ad.class));
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return null;
    }
}
