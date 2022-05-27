package com.example.springangularadsapp.audit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "stats-logs")
public class StatsLog {
    @Id
    private String id;
    @CreatedDate
    private Date createdDate;
    private String request_id;
    private String host;
    private String http_method;
    private String uri;
    private String api;
    private String arguments;
    private Long elapsedTime;

    public StatsLog() {
    }

    public StatsLog(String request_id, String host, String http_method, String uri, String api, String arguments, Long elapsedTime) {
        this.request_id = request_id;
        this.host = host;
        this.http_method = http_method;
        this.uri = uri;
        this.api = api;
        this.arguments = arguments;
        this.elapsedTime = elapsedTime;
    }
}
