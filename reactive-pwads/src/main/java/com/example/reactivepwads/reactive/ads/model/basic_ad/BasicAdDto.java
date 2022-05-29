package com.example.reactivepwads.reactive.ads.model.basic_ad;

import com.example.reactivepwads.reactive.ads.model.ad.AdDto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Data
public class BasicAdDto extends AdDto {
    @Size(min = 10, max = 60)
    @NotBlank
    private String title;

    @Size(max = 150)
    @NotBlank
    private String description;
}
