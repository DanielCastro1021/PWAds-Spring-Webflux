package com.example.reactivepwads.reactive.messages.model;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;

import com.example.reactivepwads.security.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "messages")
public  class Message {
    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @DBRef
    private User from;

    @DBRef
    private User to;

    @DBRef
    private Ad ad;

    private String message;

    public Message() {
    }


}
