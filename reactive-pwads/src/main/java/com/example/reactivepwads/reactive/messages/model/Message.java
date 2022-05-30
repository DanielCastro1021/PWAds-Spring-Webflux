package com.example.reactivepwads.reactive.messages.model;

import com.example.reactivepwads.reactive.ads.model.ad.Ad;

import com.example.reactivepwads.security.model.User;
import lombok.*;
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
@NoArgsConstructor
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    private String from;

    private String to;

    private String ad;

    private String message;
}
