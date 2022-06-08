package com.example.reactivepwads.reactive.ads.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class AdCreatedEventPublisher implements ApplicationListener<AdCreatedEvent>, Consumer<FluxSink<AdCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<AdCreatedEvent> queue = new LinkedBlockingQueue<>();

    AdCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void onApplicationEvent(AdCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<AdCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true) try {
                AdCreatedEvent event = queue.take();
                sink.next(event);
            } catch (InterruptedException e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
        });
    }

}
