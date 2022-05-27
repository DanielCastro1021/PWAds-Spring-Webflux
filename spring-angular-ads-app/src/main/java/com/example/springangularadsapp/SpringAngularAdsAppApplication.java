package com.example.springangularadsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SpringAngularAdsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAngularAdsAppApplication.class, args);
    }

}
