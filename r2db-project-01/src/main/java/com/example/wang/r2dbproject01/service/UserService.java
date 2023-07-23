package com.example.wang.r2dbproject01.service;

import com.example.wang.r2dbproject01.domain.User;
import com.example.wang.r2dbproject01.repository.UserRepository;
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

    //////保存 或者 更新////////
    @Transactional
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Flux<User> saveAll(List<User> userList) {
        Flux<User> userFlux = userRepository.saveAll(userList);
        return userFlux;
    }

    //////// 删除 //////////
    @Transactional
    public Mono<Void> delete(User user) {
        Mono<Void> delete = userRepository.delete(user);
        return delete;
    }

    @Transactional
    public Mono<Void> deleteAll(List<User> userList) {
        Mono<Void> voidMono = userRepository.deleteAll(userList);
        return voidMono;
    }

    @Transactional
    public Mono<Void> deleteById(Integer id) {
        Mono<Void> voidMono = userRepository.deleteById(id);
        return voidMono;
    }

    /////// 查找 /////////

    public Mono<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAllById(List<Integer> ids) {
        Flux<User> allById = userRepository.findAllById(ids);
        return allById;
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
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

}
