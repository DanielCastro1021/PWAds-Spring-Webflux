package com.example.reactivepwads.reactive.ads.util;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

public interface PersonalAdService<T extends Ad> {
    Flux<T> myAds(ServerRequest request);
}
