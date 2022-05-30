package com.example.reactivepwads.reactive.ads.controller;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.service.AdService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;

@RequestMapping("/api/ads")
@RestController
@AllArgsConstructor
public class StreamAdController {
    private final AdService adService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Long, Ad>> getAdByEvent() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(5));
        Flux<Ad> events = adService.findAll();
        return Flux.zip(interval, events);
    }
}
