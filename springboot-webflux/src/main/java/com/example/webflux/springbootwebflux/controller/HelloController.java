package com.example.webflux.springbootwebflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 10:45
 * @description
 */
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public Mono<String> hello() {
        return Mono.just("hello jeffrey");
    }
}
