package com.example.webflux.springbootwebflux.service;

import com.example.webflux.springbootwebflux.entity.User;
import com.example.webflux.springbootwebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 16:48
 * @description
 */
@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    @Transactional
    public Mono<User> insertByTemplate(User user) {
        return userRepository.save(user);
    }

    public Mono<User> selectByTemplate(int id) {
        return userRepository.findById(id);
    }


    public Mono<List<User>> selectByCondition() {
        Mono<List<User>> listMono = userRepository.findAll().flatMap(user -> {
            user.setName(user.getName() + "-hello");
            return Mono.just(user);
        }).filter(user -> {
            return user.getId() > 1 && user.getId() < 6;
        }).collectList();
        return listMono;
    }

    public Flux<User> selectUsersAll() {
        return userRepository.findAll();
    }
}
