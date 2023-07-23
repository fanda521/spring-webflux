package com.example.wang.r2dbproject01.repository;

import com.example.wang.r2dbproject01.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 17:35
 * @description
 */
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

}
