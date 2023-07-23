package com.example.wang.r2dbproject01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.example.wang.r2dbproject01.repository")
@EnableTransactionManagement
public class R2dbProject01Application {

    public static void main(String[] args) {
        SpringApplication.run(R2dbProject01Application.class, args);
    }

}
