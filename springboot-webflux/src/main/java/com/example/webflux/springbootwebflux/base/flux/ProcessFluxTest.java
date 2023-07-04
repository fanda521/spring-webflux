package com.example.webflux.springbootwebflux.base.flux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 18:53
 * @week 星期二
 * @description
 **/
public class ProcessFluxTest {

    /**
     * all(Predicate<? super T> predicate)
     * 如果此序列的所有值都与谓词匹配，则发出单个布尔值true。
     *
     * 用于对每个元素进行判断。如果所有元素都满足条件，
     * 则返回一个值为 true 的 Mono；否则返回一个值为 false 的 Mono。
     */
    @Test
    public void processTest01() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

        Mono<Boolean> result = flux.all(num -> num > 0);
        result.subscribe(System.out::println);  // 输出: true


        Flux<Integer> flux2 = Flux.empty();

        Mono<Boolean> result2 = flux2.all(num -> num > 0);
        result2.subscribe(System.out::println);  // 输出: true

    }

    /**
     * any(Predicate<? super T> predicate)
     * 如果此Flux序列的任何值与谓词匹配，则发出单个布尔值true。
     *
     * 用于对每个元素进行判断。如果存在至少一个元素满足条件，
     * 则返回一个值为 true 的 Mono；否则返回一个值为 false 的 Mono。
     */

    @Test
    public void processTest02() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

        Mono<Boolean> result = flux.any(num -> num > 3);
        result.subscribe(System.out::println);  // 输出: true

        Flux<Integer> flux2 = Flux.empty();

        Mono<Boolean> result2 = flux2.any(num -> num > 0);
        result2.subscribe(System.out::println);  // 输出: false


    }

}
