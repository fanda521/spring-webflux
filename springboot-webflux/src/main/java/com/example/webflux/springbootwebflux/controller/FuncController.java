package com.example.webflux.springbootwebflux.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 15:43
 * @description 函数式编程
 */
@Configuration
public class FuncController {
    @Bean
    public RouterFunction<ServerResponse> routers() {
        return RouterFunctions.route().GET("/func/greeting", new HandlerFunction<ServerResponse>() {
            @Override
            public Mono<ServerResponse> handle(ServerRequest request) {
                return ServerResponse.ok().bodyValue("hello webflux by func");
            }
        }).build();
    }
}
