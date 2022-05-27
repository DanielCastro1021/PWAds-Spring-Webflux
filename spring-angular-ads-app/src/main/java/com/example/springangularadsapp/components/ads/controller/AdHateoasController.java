package com.example.springangularadsapp.components.ads.controller;

import com.example.springangularadsapp.components.ads.assembler.AdModelAssembler;
import com.example.springangularadsapp.components.ads.model.ad.Ad;
import com.example.springangularadsapp.components.ads.model.ad.AdDto;
import com.example.springangularadsapp.components.ads.repository.AdRepository;
import com.example.springangularadsapp.controller.HateoasController;
import com.example.springangularadsapp.security.repository.UserRepository;

public abstract class AdHateoasController<T extends Ad, S extends AdDto> extends HateoasController<T, S> {
    private final AdRepository<T> repository;
    private final AdModelAssembler<T> assembler;
    private final UserRepository userRepository;

    public AdHateoasController(AdRepository<T> repository, AdModelAssembler<T> assembler, UserRepository userRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.userRepository = userRepository;
    }

    public AdRepository<T> getRepository() {
        return repository;
    }

    public AdModelAssembler<T> getAssembler() {
        return assembler;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
