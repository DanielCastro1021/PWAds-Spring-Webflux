package com.example.reactivepwads.reactive.messages.handler;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.messages.model.Message;
import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.reactive.messages.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class MessageHandler implements MessageWebfluxHandler {

    private final MessageService messageService;

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> sent(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(messageService.sent(), Message.class);
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> received(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(messageService.received(), Message.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(messageService.findAll(), Message.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(messageService.findById(id), Ad.class);
    }

    @Override
    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<MessageDto> dto = request.bodyToMono(MessageDto.class);
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(messageService::save), Message.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in MessageService.save method.")));

    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        final Mono<MessageDto> dto = request.bodyToMono(MessageDto.class);
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(dto.flatMap(messageDto -> messageService.update(messageDto, id)), Message.class)).switchIfEmpty(Mono.error(new Exception("Something went wrong in MessageService.update method")));

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(messageService.delete(id), Message.class);
    }
}
