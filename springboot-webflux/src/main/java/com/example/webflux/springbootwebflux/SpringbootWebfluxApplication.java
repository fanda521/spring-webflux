package com.example.webflux.springbootwebflux;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@SpringBootApplication
@EnableR2dbcRepositories(basePackages = {"com.example.springbootwebflux.repository"})
public class SpringbootWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebfluxApplication.class, args);
    }


}
