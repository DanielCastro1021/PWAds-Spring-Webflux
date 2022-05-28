package com.example.reactivepwads.domain.ads.model.ad;

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
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "ads")
public abstract class Ad {

    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @DBRef
    private User owner;

    private List<String> imageList;

    public Ad() {
    }

    public Ad(User owner) {
        this.owner = owner;
    }

    public boolean checkOwner(String username) {
        return Objects.equals(this.owner.getUsername(), username);
    }
}
