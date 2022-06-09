package com.example.reactivepwads.reactive.messages.service;

import com.example.reactivepwads.exceptions.MessageNotFoundException;
import com.example.reactivepwads.reactive.messages.mapper.MessageMapper;
import com.example.reactivepwads.reactive.messages.model.Message;
import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.reactive.messages.repository.MessageReactiveRepository;
import com.example.reactivepwads.reactive.messages.util.PersonalMessageService;
import com.example.reactivepwads.reactive.messages.event.MessageCreatedEvent;
import com.example.reactivepwads.reactive.util.WebfluxService;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MessageService implements WebfluxService<Message, MessageDto>, PersonalMessageService {
    private final MessageReactiveRepository repository;
    private final ApplicationEventPublisher publisher;
    private final UserReactiveRepository userReactiveRepository;
    private final MessageMapper mapper;

    @Override
    public Flux<Message> sent() {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMapMany(userDetails -> repository.findByFrom(userDetails.toString()));
    }

    @Override
    public Flux<Message> received() {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMapMany(userDetails -> repository.findByTo(userDetails.toString()));
    }

    @Override
    public Flux<Message> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Message> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Message> save(MessageDto entity) {
        return mapper.messageDtoToMessage(entity).flatMap(repository::save).doOnSuccess(message -> publisher.publishEvent(new MessageCreatedEvent(message)));
    }

    @Override
    public Mono<Message> update(MessageDto entity, String id) {
        return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMap(userDetails -> userReactiveRepository.findByUsername(userDetails.toString())).flatMap(user -> repository.findById(id)).flatMap(message -> mapper.messageDtoToMessage(entity).flatMap(newMessage -> {
            message.setMessage(newMessage.getMessage());
            message.setFrom(newMessage.getFrom());
            message.setTo(newMessage.getTo());
            message.setAd(newMessage.getAd());
            return repository.save(message);
        }));
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id).flatMap(repository::delete).switchIfEmpty(Mono.error(new MessageNotFoundException(id)));
    }
}
