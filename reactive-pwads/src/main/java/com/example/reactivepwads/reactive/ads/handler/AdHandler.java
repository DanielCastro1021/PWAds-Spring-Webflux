package com.example.reactivepwads.reactive.ads.handler;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.service.AdService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class AdHandler implements AdWebfluxHandler {
    private final AdService adService;

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> myAds(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(adService.myAds(), Ad.class);
    }

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(adService.findAll(), Ad.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(adService.findById(id), Ad.class);
    }

    @Override
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> save(ServerRequest request) {
        return null;
    }


    @Override
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> update(ServerRequest request) {
        return null;
    }


    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(adService.delete(id), Ad.class);
    }
}
