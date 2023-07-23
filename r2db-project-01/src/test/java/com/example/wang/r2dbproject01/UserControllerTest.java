package com.example.wang.r2dbproject01;

import com.example.wang.r2dbproject01.controller.UserController;
import com.example.wang.r2dbproject01.domain.User;
import com.example.wang.r2dbproject01.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * @version 1.0
 * @Aythor allen
 * @date 2023/7/22 23:17
 * @description
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Commit
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @Test
    public void save() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck")
                .sex("男")
                .password("123456")
                //.id(11)
                .build();
        System.out.println("before ...");
        System.out.println(user);
        Mono<User> save = userController.save(user);
        System.out.println("after ...");
        save.subscribe(System.out::println);
    }

    @Test
    public void saveAll() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck")
                .sex("男")
                .password("123456")
                .build();
        User user2 = User.builder()
                .age(22)
                .birthday(LocalDate.now())
                .name("annie")
                .sex("女")
                .password("123456")
                .build();
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Flux<User> save = userController.save(users);
        save.subscribe(System.out::println);

        /**
         * 2023-07-23 01:06:37.283 DEBUG 8588 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient     : Executing SQL statement [INSERT INTO t_user (t_name, t_age, t_sex, t_birthday, t_password) VALUES (?, ?, ?, ?, ?)]
         * User(id=12, name=luck, age=24, sex=男, birthday=2023-07-23, password=123456)
         * 2023-07-23 01:06:37.324 DEBUG 8588 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient     : Executing SQL statement [INSERT INTO t_user (t_name, t_age, t_sex, t_birthday, t_password) VALUES (?, ?, ?, ?, ?)]
         * User(id=13, name=annie, age=22, sex=女, birthday=2023-07-23, password=123456)
         */
    }

    @Test
    public void delete() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck")
                .sex("男")
                .password("123456")
                .id(11)
                .build();
        Mono<Void> delete = userController.delete(user);
        delete.subscribe(System.out::println);
        /**
         * 必须要主键，因为他是根据主键删除的
         * 否则报错
         * Caused by: java.lang.IllegalArgumentException: Could not obtain required identifier from entity
         * User(id=null, name=luck, age=24, sex=男, birthday=2023-07-23, password=123456)
         *
         * 2023-07-23 01:14:21.164 DEBUG 24584 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [DELETE FROM t_user WHERE t_user.id = ?]
         */
    }

    @Test
    public void deleteById() {
        Mono<Void> voidMono = userController.deleteById(12);
        voidMono.subscribe(System.out::println);
        /**
         * 2023-07-23 01:23:25.633 DEBUG 8128 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [DELETE FROM t_user WHERE t_user.id = ?]
         */
    }

    @Test
    public void deleteAll() {
        User user = User.builder().id(12).build();
        User user2 = User.builder().id(13).build();
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Mono<Void> voidMono = userController.deleteAll(users);
        voidMono.subscribe(System.out::println);
        /**
         * 2023-07-23 01:29:51.089 DEBUG 8604 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [DELETE FROM t_user WHERE t_user.id IN (?, ?)]
         */
    }
}
