package com.example.reactivepwads.reactive.messages.mapper;

import com.example.reactivepwads.exceptions.AdNotFoundException;
import com.example.reactivepwads.exceptions.UserNotFoundException;
import com.example.reactivepwads.reactive.ads.model.ad.Ad;
import com.example.reactivepwads.reactive.ads.repository.AdReactiveRepository;
import com.example.reactivepwads.reactive.messages.factory.MessageFactory;
import com.example.reactivepwads.reactive.messages.model.Message;
import com.example.reactivepwads.reactive.messages.model.MessageDto;
import com.example.reactivepwads.security.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class MessageMapper {
    private final MessageFactory factory;
    private final UserReactiveRepository userRepository;
    private final AdReactiveRepository<Ad> adRepository;


    public Message messageDtoToMessage(MessageDto dto) {
        if (dto.getTo() == null || dto.getTo().isEmpty())
            throw new UserNotFoundException(dto.getTo()); //FIXME: Message Build Error Exception
        else {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> from = this.userRepository.findByUsername(userDetails.getUsername());
            Optional<User> to = this.userRepository.findByUsername(dto.getTo());
            Optional<Ad> ad = this.adRepository.findById(dto.getAdId());
            if (from.isEmpty()) throw new UserNotFoundException(userDetails.getUsername());
            else if (to.isEmpty()) throw new UserNotFoundException(dto.getTo());
            else if (ad.isEmpty()) throw new AdNotFoundException(dto.getAdId()); //FIXME: Message Build Error Exception
            else if (dto.getMessage() == null || dto.getMessage().isEmpty())
                throw new AdNotFoundException("");  //FIXME: Message Build Error Exception
            else {
                Message msg = this.factory.getMessageInstance(dto.getClass());
                msg.setFrom(from.get());
                msg.setTo(to.get());
                msg.setMessage(dto.getMessage());
                msg.setAd(ad.get());
                return msg;
            }
        }
    }
}
