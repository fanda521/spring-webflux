package com.example.webflux.springbootwebflux.base.flux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/5
 * @time 11:23
 * @week 星期三
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonFlux {
    private String name;
    private int age;

    public String key() {
        return this.getName() + "_" + this.getAge();
    }
}
