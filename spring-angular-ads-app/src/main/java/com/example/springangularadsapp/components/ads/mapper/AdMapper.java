package com.example.springangularadsapp.components.ads.mapper;


import com.example.springangularadsapp.components.ads.model.basic_ad.BasicAd;
import com.example.springangularadsapp.components.ads.model.basic_ad.BasicAdDto;
import com.example.springangularadsapp.components.ads.model.car_ad.CarAd;
import com.example.springangularadsapp.components.ads.model.car_ad.CarAdDto;
import com.example.springangularadsapp.exceptions.UserNotFoundException;
import com.example.springangularadsapp.components.ads.factory.AdFactory;
import com.example.springangularadsapp.security.models.User;
import com.example.springangularadsapp.security.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdMapper {
    private final AdFactory adFactory;
    private final UserRepository userRepository;

    public AdMapper(AdFactory adFactory, UserRepository userRepository) {
        this.adFactory = adFactory;
        this.userRepository = userRepository;
    }

    public BasicAd basicAdDtoToBasicAd(BasicAdDto dto) throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = this.userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) throw new UserNotFoundException(userDetails.getUsername());
        else {
            BasicAd ad = (BasicAd) this.adFactory.getAdInstance(dto.getClass());
            ad.setOwner(user.get());
            if (dto.getTitle() != null) ad.setTitle(dto.getTitle());
            if (dto.getDescription() != null) ad.setDescription(dto.getDescription());
            if (dto.getImageList() != null && !dto.getImageList().isEmpty()) ad.setImageList(dto.getImageList());
            return ad;
        }
    }

    public CarAd carAdDtoToCarAd(CarAdDto dto) throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = this.userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) throw new UserNotFoundException(userDetails.getUsername());
        else {
            CarAd ad = (CarAd) this.adFactory.getAdInstance(dto.getClass());
            ad.setOwner(user.get());
            ad.setMaker(dto.getMaker());
            ad.setModel(dto.getModel());
            ad.setYear(dto.getYear());
            if (dto.getImageList() != null && !dto.getImageList().isEmpty()) ad.setImageList(dto.getImageList());
            return ad;
        }
    }
}
