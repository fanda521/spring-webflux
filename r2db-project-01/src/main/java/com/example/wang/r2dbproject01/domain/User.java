package com.example.wang.r2dbproject01.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 16:39
 * @description
 */
@Table("t_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {
    @Id
    private Integer id;
    @Column("t_name")
    private String name;
    @Column("t_age")
    private int age;
    @Column("t_sex")
    private String sex;
    @Column("t_birthday")
    private LocalDate birthday;
    @Column("t_password")
    private String password;
    @Column("t_num")
    private String num;


}
