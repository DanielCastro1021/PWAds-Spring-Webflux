package com.example.springangularadsapp.components.ads.model.car_ad;

import com.example.springangularadsapp.components.ads.model.ad.AdDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class CarAdDto extends AdDto {
    @NotBlank
    @Size(min = 10, max = 60)
    private String maker;

    @NotBlank
    @Size(min = 10, max = 60)
    private String model;

    @NotBlank
    private int year;

}
