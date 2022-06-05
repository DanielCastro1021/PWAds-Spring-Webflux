package com.example.reactivepwads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ReactivePwadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactivePwadsApplication.class, args);
    }

}
