package com.example.reactivepwads.audit.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "audit-logs")
public class AuditLog {
    @Id
    private String id;
    @CreatedDate
    private Date createdDate;

    private String request_protocol;
    private String http_method;
    private String uri;
    private String api_class;
    private String api_method;
    private String api_full_name;
    private Long elapsedTime;

    public AuditLog(String request_protocol, String http_method, String uri, String api_class, String api_method, String api_full_name, Long elapsedTime) {
        this.request_protocol = request_protocol;
        this.http_method = http_method;
        this.uri = uri;
        this.api_class = api_class;
        this.api_method = api_method;
        this.api_full_name = api_full_name;
        this.elapsedTime = elapsedTime;
    }
}
