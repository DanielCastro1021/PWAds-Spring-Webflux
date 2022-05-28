package com.example.reactivepwads.reactive.ads.util;

import com.example.reactivepwads.domain.ads.model.ad.Ad;
import com.example.reactivepwads.domain.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.repository.ReactiveAdRepository;
import com.example.reactivepwads.reactive.util.WebfluxService;
import com.example.reactivepwads.reactive.users.repository.ReactiveUserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AdWebfluxService<T extends Ad, S extends AdDto> implements WebfluxService<T, S>, PersonalAdService<T> {
    private final ReactiveAdRepository<T> repository;
    private final ReactiveUserRepository userRepository;
}