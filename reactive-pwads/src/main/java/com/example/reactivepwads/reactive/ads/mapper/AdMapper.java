package com.example.reactivepwads.reactive.ads.mapper;


import com.example.reactivepwads.exceptions.UserNotFoundException;
import com.example.reactivepwads.reactive.ads.factory.AdFactory;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAd;
import com.example.reactivepwads.reactive.ads.model.basic_ad.BasicAdDto;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAd;
import com.example.reactivepwads.reactive.ads.model.car_ad.CarAdDto;
import com.example.reactivepwads.security.model.User;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Component
public class AdMapper {
    private final AdFactory adFactory;
    private final UserReactiveRepository userRepository;


    public Mono<BasicAd> basicAdDtoToBasicAd(BasicAdDto dto) throws UserNotFoundException {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMap(userDetails -> userRepository.findByUsername(userDetails.toString())).map(user -> mapDtoToBasicAd(dto, user));
    }

    public Mono<CarAd> carAdDtoToCarAd(CarAdDto dto) throws UserNotFoundException {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMap(userDetails -> userRepository.findByUsername(userDetails.toString()).map(user -> mapDtoToCarAd(dto, user)));
    }

    private BasicAd mapDtoToBasicAd(BasicAdDto dto, User user) {
        BasicAd ad = (BasicAd) this.adFactory.getAdInstance(BasicAdDto.class);
        ad.setOwner(user.getUsername());
        if (dto.getTitle() != null) ad.setTitle(dto.getTitle());
        if (dto.getDescription() != null) ad.setDescription(dto.getDescription());
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) ad.setImageList(dto.getImageList());
        return ad;
    }

    private CarAd mapDtoToCarAd(CarAdDto dto, User user) {
        CarAd ad = (CarAd) this.adFactory.getAdInstance(CarAdDto.class);
        ad.setOwner(user.getUsername());
        ad.setMaker(dto.getMaker());
        ad.setModel(dto.getModel());
        ad.setYear(dto.getYear());
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) ad.setImageList(dto.getImageList());
        return ad;
    }
}
