package com.example.springangularadsapp.components.messages.mapper;

import com.example.springangularadsapp.components.ads.model.ad.Ad;
import com.example.springangularadsapp.components.ads.repository.AdRepository;
import com.example.springangularadsapp.components.messages.model.Message;
import com.example.springangularadsapp.components.messages.model.MessageDto;
import com.example.springangularadsapp.components.messages.factory.MessageFactory;
import com.example.springangularadsapp.exceptions.AdNotFoundException;
import com.example.springangularadsapp.exceptions.UserNotFoundException;
import com.example.springangularadsapp.security.models.User;
import com.example.springangularadsapp.security.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageMapper {
    private final MessageFactory factory;
    private final UserRepository userRepository;
    private final AdRepository<Ad> adRepository;

    public MessageMapper(MessageFactory factory, UserRepository userRepository, AdRepository<Ad> adRepository) {
        this.factory = factory;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

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