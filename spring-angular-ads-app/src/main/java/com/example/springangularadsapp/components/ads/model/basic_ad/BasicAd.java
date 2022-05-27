package com.example.springangularadsapp.components.ads.model.basic_ad;

import com.example.springangularadsapp.components.ads.model.ad.Ad;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@TypeAlias("basic-ad")
@Document(collection = "ads")
public class BasicAd extends Ad {
    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(max = 150)
    private String description;
}
