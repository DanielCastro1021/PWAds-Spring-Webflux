package com.example.reactivepwads.reactive.ads.util;

import com.example.reactivepwads.reactive.ads.mapper.AdMapper;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.reactive.util.WebfluxService;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AdWebfluxService<T extends Ad, S extends AdDto> implements WebfluxService<T, S>, PersonalAdService<T> {

    private final AdReactiveRepository<T> repository;

    private final UserReactiveRepository userRepository;

    private final AdMapper adMapper;
}
