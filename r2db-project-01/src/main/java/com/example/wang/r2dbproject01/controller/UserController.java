package com.example.wang.r2dbproject01.controller;

import com.example.wang.r2dbproject01.domain.User;
import com.example.wang.r2dbproject01.service.UserService;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/save")
    public Mono<User> save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/saveAll")
    public Flux<User> save(@RequestBody List<User> userList) {
        return userService.saveAll(userList);
    }

    @DeleteMapping("/delete")
    public Mono<Void> delete(@RequestBody User user){
        return userService.delete(user);
    }

    @DeleteMapping("/deleteById")
    public Mono<Void> deleteById(@RequestParam("id") int id) {
        return userService.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public Mono<Void> deleteAll(@RequestBody List<User> userList) {
        return userService.deleteAll(userList);
    }

    @PostMapping("/getUserById")
    public Mono<User> selectUserById(@RequestParam("id") int id) {
        return userService.findById(id);
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
        return userService.findAll();
    }
}
