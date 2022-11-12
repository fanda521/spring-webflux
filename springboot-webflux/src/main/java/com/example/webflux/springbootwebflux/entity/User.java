package com.example.webflux.springbootwebflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class User {
    @Id
    private int id;
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


}
