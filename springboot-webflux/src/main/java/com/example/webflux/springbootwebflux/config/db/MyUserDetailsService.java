package com.example.webflux.springbootwebflux.config.db;

import com.example.webflux.springbootwebflux.entity.User;
import com.example.webflux.springbootwebflux.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/18 22:57
 * @description 主要获取数据库的用户信息
 */
//@Service
public class MyUserDetailsService implements ReactiveUserDetailsService {
    @Resource
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_sale1,admin");
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, new BCryptPasswordEncoder().encode("123456"), auths);
        return Mono.just(user);
    }
}
