package com.example.webflux.springbootwebflux.base.flux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/5
 * @time 10:59
 * @week 星期三
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomObjectFlux {

    private List<Integer> positives = new ArrayList<>();
    private List<Integer> negatives = new ArrayList<>();


    public void addPositive(int num) {
        positives.add(num);
    }

    public void addNegative(int num) {
        negatives.add(num);
    }

}
