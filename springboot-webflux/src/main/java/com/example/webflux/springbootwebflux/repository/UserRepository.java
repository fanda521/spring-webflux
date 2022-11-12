package com.example.webflux.springbootwebflux.repository;

import com.example.webflux.springbootwebflux.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 17:35
 * @description
 */
public interface UserRepository extends ReactiveCrudRepository<User,Integer> {
    Mono<User> findByName(String name);
}
