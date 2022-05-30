package com.example.reactivepwads.reactive.ads.handler;

import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.service.BasicAdService;
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
public class BasicAdHandler implements AdWebfluxHandler {
    private final BasicAdService basicAdService;

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> myAds(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(basicAdService.myAds(), BasicAd.class);
    }

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(basicAdService.findAll(), BasicAd.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(basicAdService.findById(id), BasicAd.class);
    }


    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<BasicAdDto> dto = request.bodyToMono(BasicAdDto.class);
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(basicAdService::save), BasicAd.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in BasicAdService.save method.")));

    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> update(ServerRequest request) {
        final Mono<BasicAdDto> dto = request.bodyToMono(BasicAdDto.class);
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(adDto -> basicAdService.update(adDto, id)), BasicAd.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in BasicAdService.update method")));

    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(basicAdService.delete(id), BasicAd.class);
    }
}
