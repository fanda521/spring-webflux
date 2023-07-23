package com.example.wang.r2dbproject01.service;

import com.example.wang.r2dbproject01.domain.User;
import io.r2dbc.spi.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.FetchSpec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

/**
 * @version 1.0
 * @Aythor allen
 * @date 2023/7/23 1:35
 * @description
 */
@Service
public class UserClientService {
    @Resource
    private DatabaseClient client;






    public Mono<Void> clientInsert(User user) {
        DatabaseClient.GenericExecuteSpec sql = client.sql("insert into t_user (t_name,t_age,t_sex,t_birthday,t_password) values ('"+ user.getName() + "'," + user.getAge() + ",'" + user.getSex() + "','" + user.getBirthday() + "', '" + user.getPassword() + "')");
        /*sql.bind("name","smile11");
        sql.bind(1,18);
        sql.bind(2,"男");
        sql.bind(3, LocalDate.now());
        sql.bind(4,"123456");*/
        FetchSpec<Map<String, Object>> fetch = sql.fetch();
        fetch.rowsUpdated().subscribe(System.out::println);
        return Mono.empty();
    }

    private Function<Row,User> mappingFunc  = row -> {
        User user = User.builder()
                .id(row.get("t_id", Integer.class))
                .name(row.get("t_name", String.class))
                .password(row.get("t_password", String.class))
                .sex(row.get("t_sex", String.class))
                .birthday(row.get("t_birthday", LocalDate.class))
                .build();
        return user;
    };
    public void findAll() {
        DatabaseClient.GenericExecuteSpec sql = client.sql("select * from t_user");
        sql.map(mappingFunc);
        FetchSpec<Map<String, Object>> fetch = sql.fetch();
        Mono<Integer> integerMono = fetch.rowsUpdated();
        integerMono.subscribe(System.out::println);

        Flux<Map<String, Object>> all = fetch.all();
        all.subscribe(System.out::println);
    }
}
