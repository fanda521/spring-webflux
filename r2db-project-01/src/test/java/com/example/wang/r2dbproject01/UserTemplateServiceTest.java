package com.example.wang.r2dbproject01;

import com.example.wang.r2dbproject01.domain.User;
import com.example.wang.r2dbproject01.service.UserClientService;
import com.example.wang.r2dbproject01.service.UserTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @version 1.0
 * @Aythor allen
 * @date 2023/7/23 1:45
 * @description
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Commit
public class UserTemplateServiceTest {
    @Autowired
    private UserTemplateService userTemplateService;

    @Test
    public void insert() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck")
                .sex("男")
                .password("123456")
                .build();
        userTemplateService.insert(user);
        /**
         * 2023-07-23 10:54:41.267 DEBUG 15716 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [INSERT INTO t_user (t_name, t_age, t_sex, t_birthday, t_password) VALUES (?, ?, ?, ?, ?)]
         * User(id=17, name=luck, age=24, sex=男, birthday=2023-07-23, password=123456)
         */
    }

    @Test
    public void insertClass() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck2")
                .sex("男")
                .password("123456")
                .build();
        userTemplateService.insertClass(user);
        /**
         * 2023-07-23 11:19:39.648 DEBUG 23812 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [INSERT INTO t_user (t_name, t_age, t_sex, t_birthday, t_password) VALUES (?, ?, ?, ?, ?)]
         * User(id=18, name=luck2, age=24, sex=男, birthday=2023-07-23, password=123456)
         */
    }
    @Test
    public void insertClass2() {
        User user = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck2")
                .sex("男")
                .password("123456")
                .build();
        User user2 = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck3")
                .sex("男")
                .password("123456")
                .build();
        User user3 = User.builder()
                .age(24)
                .birthday(LocalDate.now())
                .name("luck4")
                .sex("男")
                .password("123456")
                .build();
        ArrayList<User> users = new ArrayList<>(Arrays.asList(user, user2, user3));
        userTemplateService.insertClass2(users);
        /**
         * 2023-07-23 12:53:36.297 DEBUG 24944 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [INSERT INTO t_user (t_name, t_age, t_sex, t_birthday, t_password) VALUES (?, ?, ?, ?, ?)]
         * User(id=null, name=luck3, age=24, sex=男, birthday=2023-07-23, password=123456, num=null)
         * User(id=null, name=luck4, age=24, sex=男, birthday=2023-07-23, password=123456, num=null)
         * 值插入了一行
         * 其他两个被打印消费了
         */
    }

    @Test
    public void update() {
        User user = User.builder()
                .name("sam")
                .num("23")
                .id(1)
                .build();
        userTemplateService.update(user);
        /**
         * 2023-07-23 11:50:35.666 DEBUG 3272 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [UPDATE t_user SET t_name = ?, t_age = ?, t_sex = ?, t_birthday = ?, t_password = ?, t_num = ? WHERE t_user.id = ?]
         * User(id=1, name=sam, age=0, sex=null, birthday=null, password=null, num=23)
         */
    }

    @Test
    public void updateQuery() {
        User user = User.builder()
                .name("jack")
                .num("23")
                .build();
        userTemplateService.updateQuery(user);
        /**
         * 2023-07-23 11:46:10.278 DEBUG 13420 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [UPDATE t_user SET t_name = ?, t_num = ? WHERE t_user.id >= ?]
         * 11
         */
    }

    @Test
    public void delete() {
        User user = User.builder()
                .id(1)
                .build();
        userTemplateService.delete(user);
        /**
         * 2023-07-23 12:11:08.912 DEBUG 17680 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [DELETE FROM t_user WHERE t_user.id = ?]
         * User(id=1, name=null, age=0, sex=null, birthday=null, password=null, num=null)
         */
    }

    @Test
    public void deleteQuery() {
        User user = User.builder()
                .id(2)
                .build();
        userTemplateService.deleteQuery(user);
        /**
         * 2023-07-23 12:13:42.298 DEBUG 19460 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [DELETE FROM t_user WHERE t_user.id = ? AND t_user.t_num IS NULL]
         * 1
         */
    }

    @Test
    public void selectOne() {
        User user = User.builder()
                .id(1)
                .name("jack")
                .build();
        userTemplateService.selectOne(user);
        /**
         * 2023-07-23 12:29:58.696 DEBUG 18324 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [SELECT t_user.* FROM t_user WHERE (t_user.id >= ? AND (t_user.t_name = ?)) LIMIT 2]
         * .IncorrectResultSizeDataAccessException: Query [SELECT t_user.* FROM t_user WHERE (t_user.id >= ? AND (t_user.t_name = ?)) LIMIT 2]
         * returned non unique result.
         */
    }

    @Test
    public void selectQuery() {
        User user = User.builder()
                .id(13)
                .name("jack")
                .build();
        userTemplateService.selectQuery(user);
    }


}
