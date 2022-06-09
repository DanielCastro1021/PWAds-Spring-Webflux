package com.example.reactivepwads.reactive.ads.model.car_ad;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@TypeAlias("car-ad")
@Document(collection = "ads")
public class CarAd extends Ad {
    @NotBlank
    @Size(max = 60)
    private String maker;
    @NotBlank
    @Size(max = 60)
    private String model;
    @NotBlank
    @Size(max = 60)
    private int year;
}
