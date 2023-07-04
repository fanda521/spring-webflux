package com.example.webflux.springbootwebflux.base.mono.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 12:33
 * @week 星期二
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMono {
    private Long id;

    private String name;

    private BigDecimal price;

    private List<String> address;
}
