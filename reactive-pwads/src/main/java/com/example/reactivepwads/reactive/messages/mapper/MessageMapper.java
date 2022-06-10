package com.example.reactivepwads.reactive.messages.mapper;

import com.example.reactivepwads.exceptions.UserNotFoundException;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.reactive.messages.factory.MessageFactory;
import com.example.reactivepwads.reactive.messages.model.Message;
import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.security.model.User;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MessageMapper {
    private final MessageFactory factory;
    private final UserReactiveRepository userRepository;
    private final AdReactiveRepository<Ad> adRepository;

    public Mono<Message> messageDtoToMessage(MessageDto dto) {
        if (dto.getTo() == null || dto.getTo().isEmpty())
            return Mono.error(new UserNotFoundException(dto.getTo())); //FIXME: Message Build Error Exception
        else {
            Mono<User> monoRequestUser = ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication().getPrincipal()).flatMap(userDetails -> userRepository.findByUsername(userDetails.toString()));
            Mono<User> monoToUser = userRepository.findByUsername(dto.getTo());
            Mono<Ad> monoAd = adRepository.findById(dto.getAdId());

            return Mono.zip(monoRequestUser, monoToUser, monoAd).flatMap(objects -> {
                User from = objects.getT1();
                User to = objects.getT2();
                Ad ad = objects.getT3();
                return Mono.just(mapDtoToMessage(dto, from, to, ad));
            });

        }
    }

    private Message mapDtoToMessage(MessageDto dto, User from, User to, Ad ad) {
        Message msg = this.factory.getMessageInstance(dto.getClass());
        msg.setFrom(from.getUsername());
        msg.setTo(to.getUsername());
        msg.setMessage(dto.getMessage());
        msg.setAd(ad.getId());
        return msg;
    }
}
