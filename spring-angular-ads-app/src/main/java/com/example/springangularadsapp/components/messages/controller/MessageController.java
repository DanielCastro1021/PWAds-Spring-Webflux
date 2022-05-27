package com.example.springangularadsapp.components.messages.controller;

import com.example.springangularadsapp.components.messages.model.Message;
import com.example.springangularadsapp.components.messages.model.MessageDto;
import com.example.springangularadsapp.components.messages.repository.MessageRepository;
import com.example.springangularadsapp.components.messages.assembler.MessageModelAssembler;

import com.example.springangularadsapp.components.messages.mapper.MessageMapper;
import com.example.springangularadsapp.controller.HateoasController;
import com.example.springangularadsapp.exceptions.MessageNotFoundException;
import com.example.springangularadsapp.security.authorization.annotation.AdminAccess;
import com.example.springangularadsapp.security.authorization.annotation.UserAccess;
import com.example.springangularadsapp.security.models.User;
import com.example.springangularadsapp.security.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/messages")
public class MessageController extends HateoasController<Message, MessageDto> {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageModelAssembler assembler;
    private final MessageMapper mapper;

    public MessageController(MessageRepository messageRepository, UserRepository userRepository, MessageModelAssembler assembler, MessageMapper mapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
        this.mapper = mapper;
    }

    @AdminAccess
    @GetMapping("/")
    public CollectionModel<EntityModel<Message>> all() {
        List<EntityModel<Message>> messages = this.messageRepository.findAll().stream().map(this.assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).all()).withSelfRel());
    }

    @UserAccess
    @GetMapping("/{id}")
    public EntityModel<Message> one(@PathVariable String id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = this.messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException(id));
        if (Objects.equals(message.getFrom().getUsername(), userDetails.getUsername()) || Objects.equals(message.getTo().getUsername(), userDetails.getUsername()))
            return this.assembler.toModel(message);
        else
            throw new MessageNotFoundException(""); // FIXME: Throw exception Message Not owned by user that requested it.
    }

    @UserAccess
    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody MessageDto newEntity) {
        EntityModel<Message> entityModel = this.assembler.toModel(this.messageRepository.save(this.mapper.messageDtoToMessage(newEntity)));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @UserAccess
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody MessageDto newEntity, @PathVariable String id) {
        Message newMsg = this.mapper.messageDtoToMessage(newEntity);
        Message updatedMsg = this.messageRepository.findById(id).map(msg -> {
            msg.setMessage(newMsg.getMessage());
            msg.setFrom(newMsg.getFrom());
            msg.setTo(newMsg.getTo());
            return this.messageRepository.save(msg);
        }).orElseGet(() -> {
            newMsg.setId(id);
            return this.messageRepository.save(newMsg);
        });

        EntityModel<Message> entityModel = this.assembler.toModel(updatedMsg);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @AdminAccess
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.messageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @UserAccess
    @GetMapping("/sent")
    public CollectionModel<EntityModel<Message>> allSent() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = this.userRepository.findByUsername(userDetails.getUsername());
        List<EntityModel<Message>> messages = this.messageRepository.findByFrom(user.get()).stream().map(this.assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).all()).withSelfRel());
    }

    @UserAccess
    @GetMapping("/received")
    public CollectionModel<EntityModel<Message>> allReceived() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = this.userRepository.findByUsername(userDetails.getUsername());
        List<EntityModel<Message>> messages = this.messageRepository.findByTo(user.get()).stream().map(this.assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).all()).withSelfRel());
    }

}
