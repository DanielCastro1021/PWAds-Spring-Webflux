package com.example.reactivepwads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;


@SpringBootApplication
@EnableReactiveMongoAuditing
public class ReactivePwadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactivePwadsApplication.class, args);
    }

}
