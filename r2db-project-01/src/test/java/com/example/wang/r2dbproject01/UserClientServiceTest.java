package com.example.wang.r2dbproject01;

import com.example.wang.r2dbproject01.domain.User;
import com.example.wang.r2dbproject01.service.UserClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * @version 1.0
 * @Aythor allen
 * @date 2023/7/23 1:45
 * @description client的方式无法绑定参数
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Commit
public class UserClientServiceTest {
    @Autowired
    private UserClientService userClientService;

    @Test
    public void clientInsert() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck")
                .sex("男")
                .password("123456")
                .build();
        Mono<Void> voidMono = userClientService.clientInsert(user);
        voidMono.subscribe(System.out::println);
    }


    @Test
    public void findAll() {
        userClientService.findAll();
    }
}
