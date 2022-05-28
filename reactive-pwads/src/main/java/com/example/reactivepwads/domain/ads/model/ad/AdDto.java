package com.example.reactivepwads.domain.ads.model.ad;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Data
public class AdDto {
    private List<String> imageList;
    private String ownerUsername;
}
