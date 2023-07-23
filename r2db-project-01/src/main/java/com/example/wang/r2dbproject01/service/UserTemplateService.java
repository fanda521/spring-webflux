package com.example.wang.r2dbproject01.service;

import com.example.wang.r2dbproject01.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @version 1.0
 * @Aythor allen
 * @date 2023/7/23 10:50
 * @description
 */
@Service
public class UserTemplateService {
    @Autowired
    private R2dbcEntityTemplate template;

    ///// 插入 //////
    public Mono<User> insert(User user) {
        Mono<User> insert = template.insert(user);
        insert.subscribe(System.out::println);
        return insert;
    }

    public Mono<User> insertClass(User user) {
        Mono<User> userMono = template.insert(User.class)
                .into("t_user")
                .using(user);
        userMono.subscribe(System.out::println);
        return userMono;
    }

    /////// 更新 ///////

    public Mono<User> update(User user) {
        Mono<User> update = template.update(user);
        update.subscribe(System.out::println);
        return update;
    }

    public Mono<Integer> updateQuery(User user) {
        Criteria criteria = Criteria.where("id").greaterThanOrEquals(5);
        Mono<Integer> apply = template.update(User.class)
                .inTable("t_user")
                .matching(Query.query(CriteriaDefinition.from(criteria)))
                .apply(Update.update("t_name", user.getName()).set("t_num", user.getNum()));
        apply.subscribe(System.out::println);
        return apply;
    }

    //////// 删除 //////////
    public Mono<User> delete(User user) {
        Mono<User> delete = template.delete(user);
        //返回的delete 是user，构造的入参 不是删除的记录
        delete.subscribe(System.out::println);
        return delete;
    }

    public Mono<Integer> deleteQuery(User user) {
        Criteria criteria = Criteria.where("id").is(user.getId()).and("t_num").isNull();
        Query query = Query.query(criteria);
        Mono<Integer> delete = template.delete(query, User.class);
        delete.subscribe(System.out::println);
        return delete;
    }

    //////////// 查询 ///////////
    public Mono<User> selectOne(User user) {
        Criteria criteria = Criteria.where("id").greaterThanOrEquals(user.getId());
        Criteria criteria1 = Criteria.where("t_name").is(user.getName());
        Criteria from = Criteria.from(criteria, criteria1);
        Query query = Query.query(from);
        Mono<User> mono = template.selectOne(query,User.class);
        mono.subscribe(System.out::println);
        return mono;
    }

    public Flux<User> selectQuery(User user) {
        Criteria criteria = Criteria.where("id").greaterThanOrEquals(user.getId());
        Criteria criteria1 = Criteria.where("t_name").is(user.getName());
        Criteria from = Criteria.from(criteria, criteria1);
        Query query = Query.query(from);
        Flux<User> select = template.select(query, User.class);
        select.subscribe(System.out::println);
        return select;
        /**
         * 2023-07-23 12:38:01.285 DEBUG 19388 --- [actor-tcp-nio-2] o.s.r2dbc.core.DefaultDatabaseClient
         * : Executing SQL statement [SELECT t_user.* FROM t_user WHERE (t_user.id >= ? AND (t_user.t_name = ?))]
         * User(id=14, name=jack, age=22, sex=男, birthday=2020-12-09, password=123456, num=23)
         * User(id=15, name=jack, age=24, sex=男, birthday=2023-07-23, password=123456, num=23)
         * User(id=16, name=jack, age=24, sex=男, birthday=2023-07-23, password=123456, num=23)
         * User(id=17, name=jack, age=24, sex=男, birthday=2023-07-23, password=123456, num=23)
         * User(id=18, name=jack, age=24, sex=男, birthday=2023-07-23, password=123456, num=23)
         */
    }



}
