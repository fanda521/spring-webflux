package com.example.webflux.springbootwebflux.controller;

import com.example.webflux.springbootwebflux.entity.User;
import com.example.webflux.springbootwebflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 16:54
 * @description
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/insertUser")
    public Mono<User> insertByTemplate(@RequestBody User user) {
        return userService.insertByTemplate(user);
    }

    @PostMapping("/getUserById")
    public Mono<User> selectUserById(@RequestParam("id") int id) {
        return userService.selectByTemplate(id);
    }

    @PostMapping("/selectByCondition")
    public Mono<List<User>> selectByCondition() {
        Mono<List<User>> listMono = userService.selectByCondition();
        List<User> block = listMono.block();
        List<User> users = new ArrayList<>();
        for (User user : block) {
            System.out.println(user);
            users.add(user);
        }
        Mono<List<User>> just = Mono.just(users);
        return just;


    }

    @PostMapping("/selectUsersAll")
    public Flux<User> selectUsersAll() {
        return userService.selectUsersAll();
    }
}
