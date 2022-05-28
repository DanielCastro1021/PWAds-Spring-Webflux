package com.example.reactivepwads.reactive.ads.handler;

import com.example.reactivepwads.reactive.util.WebfluxHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface AdWebfluxHandler extends WebfluxHandler {
    public Mono<ServerResponse> myAds(ServerRequest request);
}
