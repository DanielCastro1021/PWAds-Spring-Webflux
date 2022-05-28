package com.example.reactivepwads.reactive.ads.util;

import com.example.reactivepwads.domain.ads.model.ad.Ad;
import reactor.core.publisher.Flux;

public interface PersonalAdService<T extends Ad> {
    Flux<T> myAds();
}
