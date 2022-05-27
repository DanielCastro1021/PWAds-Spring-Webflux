package com.example.springangularadsapp.components.messages.assembler;

import com.example.springangularadsapp.components.messages.controller.MessageController;
import com.example.springangularadsapp.components.messages.model.Message;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {
    @Override
    public EntityModel<Message> toModel(Message message) {
        return EntityModel.of(message, WebMvcLinkBuilder.linkTo(methodOn(MessageController.class).one(message.getId())).withSelfRel()
                , linkTo(methodOn(MessageController.class).all()).withRel("messages"));
    }
}
