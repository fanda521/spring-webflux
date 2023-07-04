package com.example.webflux.springbootwebflux.base.mono.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 15:13
 * @week 星期二
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMono {

    private String orderId;
    private List<String> items;


}
