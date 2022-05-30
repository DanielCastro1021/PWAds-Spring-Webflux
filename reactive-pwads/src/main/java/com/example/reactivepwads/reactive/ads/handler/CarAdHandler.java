package com.example.reactivepwads.reactive.ads.handler;

import com.example.reactivepwads.reactive.ads.model.car_ad.CarAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.reactive.ads.service.CarAdService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CarAdHandler implements AdWebfluxHandler {
    private final CarAdService carAdService;

    public CarAdHandler(CarAdService carAdService) {
        this.carAdService = carAdService;
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> myAds(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(carAdService.myAds(), CarAd.class);
    }

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(carAdService.findAll(), CarAd.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(carAdService.findById(id), CarAd.class);
    }


    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<CarAdDto> dto = request.bodyToMono(CarAdDto.class);
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(carAdService::save), CarAd.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in CarAdService.save method.")));

    }

    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        final Mono<CarAdDto> dto = request.bodyToMono(CarAdDto.class);
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(adDto -> carAdService.update(adDto, id)), CarAd.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in CarAdService.update method")));
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(carAdService.delete(id), CarAd.class);
    }
}
