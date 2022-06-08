package com.example.reactivepwads.reactive.ads.event;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import org.springframework.context.ApplicationEvent;

public class AdCreatedEvent extends ApplicationEvent {
    public AdCreatedEvent(Ad source) {
        super(source);
    }
}
