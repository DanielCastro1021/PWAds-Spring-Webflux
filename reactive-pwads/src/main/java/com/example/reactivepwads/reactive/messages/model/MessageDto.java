package com.example.reactivepwads.reactive.messages.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MessageDto {
    private String to;

    private String adId;

    private String message;
}
