package com.example.webflux.springbootwebflux.base;

import com.example.webflux.springbootwebflux.entity.User;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 11:20
 * @description Mono 和 Flux 的使用
 */
public class MonoAndFlux {

    @Test
    public void createMono() {
        Mono.empty().subscribe(System.out::println);
        Mono.just("hello jeffrey").subscribe(System.out::println);
    }

    @Test
    public void createFlux() {
        Flux.just(1,2,3,4,5).subscribe(System.out::println);
        System.out.println("-----------------");
        Flux.fromIterable(Arrays.asList("a","b","c")).subscribe(System.out::print);
        System.out.println("-------------------");
        Flux.fromArray(new String[] {"11","22" }).subscribe(System.out::print);
        System.out.println("---------------------");
        Flux.fromStream(Stream.of(1,23,3,4)).subscribe(System.out::print);
        System.out.println("--------------------");
        Flux.range(1,10).subscribe(System.out::print);
    }

    @Test
    public void transfer() {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        just.subscribe(System.out::println);
        Mono<List<Integer>> mono = just.collectList();
        mono.subscribe(System.out::println);
        //自定义收集器
        /*Mono<List<Integer>> monoList = just.collect(toList());
        monoList.subscribe(System.out::println);*/
        //Mono转仅有一个元素的Flux
        Flux<List<Integer>> flux = mono.flux();
        flux.subscribe(System.out::println);
        //将一个元素的Flux转Mono
        Mono<Integer> single = Flux.just(1).single();
        single.subscribe(System.out::println);

    }

    @Test
    public void flueInterval() throws InterruptedException {
        //interval() 方法可以用来生成从 0 开始递增的 Long 对象的数据序列
        Flux.interval(Duration.ofMillis(200))
                //  map可以对数据进行处理
                .map(i->"执行内容："+i)
                //限制执行5次
                .take(5)
                .subscribe(System.out::println);
                //避免主线程提前结束
        Thread.sleep(1100);

    }

    @Test
    public void flueDelay() throws InterruptedException {
        Flux.fromIterable(Arrays.asList(1,2,3,4))
                //延时发送
                .delayElements(Duration.ofMillis(100L))
                .subscribe(System.out::println);
        //避免主线程提前结束
        Thread.sleep(1100);
    }

    @Test
    public void flueGenerate() {
        /**
         * generate() 方法生成 Flux 序列依赖于 Reactor 所提供的 SynchronousSink 组件，定义如下。
         * SynchronousSink 组件包括 next()、complete() 和 error() 这三个核心方法。
         * 从 SynchronousSink 组件的命名上就能知道它是一个同步的 Sink 组件，
         * 也就是说元素的生成过程是同步执行的。这里要注意的是 next() 方法只能最多被调用一次。
         * 使用 generate() 方法创建 Flux 的示例代码如下。
         *
         */
        // 同步动态创建， next() 方法只能最多被调用一次
        Flux.generate(sink -> {
            sink.next("1");
            //第二次会报错:
            //java.lang.IllegalStateException: More than one call to onNext
            //sink.next("2");
            //如果不调用 complete() 方法，那么就会生成一个所有元素均为“1”的无界数据流
            sink.complete();
        }).subscribe(System.out::println);

        Flux.generate(() -> 1, (i, sink) -> {
            sink.next(i);
            if (i == 5) {
                sink.complete();
            }
            return ++i;
        }).subscribe(System.out::println);

    }

    @Test
    public void fluxCreate() {
        Flux.create(sink -> {
            for (int i = 0; i < 5; i++) {
                sink.next("Tang" + i);
            }
            sink.complete();
        }).subscribe(System.out::println);

    }

    @Test
    public void monoJustOrEmpty() {
        Mono.justOrEmpty(Optional.of("Tang")).subscribe(System.out::println);
    }

    @Test
    public void monoCreate() {
        Mono.create(sink -> sink.success("Tang")).subscribe(System.out::println);
    }

    @Test
    public void monoFromCallable() {
        Mono.fromCallable(() -> {
            Thread.sleep(1000);
            return "1";
        }).subscribe(System.out::println);
    }

    @Test
    public void exceptionMonoTest01() {
        Mono.just("1")
                //连接一个包含异常的Mono
                .concatWith(Mono.error(new Exception("Exception")))
                //异常监听
                .doOnError(error -> System.out.println("错误: " + error))
                //在发生异常时将其入参传递给订阅者
                .onErrorReturn("ErrorReturn")
                .subscribe(System.out::println);

    }

    @Test
    public void exceptionFluxTest01() {
        Flux.just(1,2,3,4)
                .concatWith(Mono.error(new Exception("Exception")))
                /*.doOnError(error -> System.out.println("错误: " + error))
                //在发生异常时将其入参传递给订阅者
                .onErrorReturn(404)*/
                .subscribe(System.out::println,System.err::println,()-> System.out.println("完成"));
    }

    @Test
    public void exceptionFluxTest02() {
        Flux.just(1,2,3,4)
                .concatWith(Mono.error(new Exception("Exception")))
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Flux.just(11,12,13);
                })
                .subscribe(System.out::println);
    }


    @Test
    public void exceptionFluxTest03() {
        Flux.just(1,2,3,4)
                .concatWith(Mono.error(new Exception("Exception")))
                .retry(1)
                .subscribe(System.out::println);
    }


    @Test
    public void fluxMege() throws InterruptedException {
        /**
         * merge按照所有流中元素的实际产生序列来合并
         */
        Flux.merge(Flux.interval(Duration.ofMillis(10)).map(i->"执行内容1："+i).take(5),
                Flux.interval(Duration.ofMillis(10)).map(i->"执行内容2："+i).take(3))
                .log()
                .subscribe();
        Thread.sleep(1000);

    }

    @Test
    public void flueMergeSequential() throws InterruptedException {
        /**
         * mergeSequential按照所有流被订阅的顺序，以流为单位进行合并。
         * 例如： FluxA 和FluxB 只有在A消费完后才会去消费B
         */

        Flux.mergeSequential(Flux.interval(Duration.ofMillis(10)).map(i->"执行内容1："+i).take(5),
                Flux.interval(Duration.ofMillis(10)).map(i->"执行内容2："+i).take(3))
                .log()
                .subscribe();
        Thread.sleep(1000);
    }

    @Test
    public void fluxmergeComparing() throws InterruptedException {
        /**
         * 消费两个流中较小的那个
         */
        Flux.mergeComparing(Flux.just(1,2,9,4,76,6),
                Flux.just(2,75,4,3,5,6))
                .log()
                .subscribe();
        Thread.sleep(1000);

    }

    @Test
    public void flueBuffer() {
        /**
         * 当maxSize > skip 时 重叠 如3,2 [1,2,3],[3,4,5],[5,6,7],[7,8,9],[9,10]
         * 当maxSize < skip 时 重叠 如3,4 [1,2,3],[5,6,7],[9,10]
         * 当maxSize = skip 时 准确分割 等价于只传maxSize 如3,3 [1,2,3],[4,5,6],[7,8,9],[10]
         */
        Flux.range(1, 10)
//	.buffer(3,2)
//	.buffer(3,4)
//	.buffer(3,3)
                .buffer(2)
                .subscribe(System.out::println);

    }

    @Test
    public void fluxBufferTimeout() throws InterruptedException {
        Flux.interval(Duration.ofMillis(100L))
                .bufferTimeout(9,Duration.ofMillis(1000L))
                .subscribe(System.out::println);
        Thread.sleep(10000);

    }

    @Test
    public void fluxBufferWhile() {
        Disposable subscribe = Flux.range(1, 10)
                .bufferWhile(i -> i % 2 == 0)
                .subscribe(System.out::println);

    }

    @Test
    public void fluxBufferUtil() {
        Flux.range(1, 10)
                .bufferUntil(i -> i % 2 == 0)
                .subscribe(System.out::println);

        Flux.range(1, 10)
                .bufferUntil(i -> i % 2 == 0,true)
                .subscribe(System.out::println);

    }

    @Test
    public void fluxFilter() {
        Flux.range(1, 10).filter(i -> i%2 == 0).subscribe(System.out::println);

    }

    @Test
    public void fluxZipWith() {
        Flux.just(1, 2)
                .zipWith(Flux.just(3, 4))
                .subscribe(System.out::println);
//通过BiFunction函数对合并的元素进行处理
        Flux.just(1, 2,3)
                .zipWith(Flux.just(4, 5), (s1, s2) -> s1 + "-" + s2).
        subscribe(System.out::println);

    }

    @Test
    public void fluxRedure() {
        Flux.range(1, 100)
                .reduce((x, y) -> x + y)
                .subscribe(System.out::println);

//设定默认值
        Flux.range(1, 100)
                .reduce(100,(x, y) -> x + y)
                .subscribe(System.out::println);

//可以设置Supplier初始值
        Flux.range(1, 100)
                .reduceWith(() -> 100, (x ,y) -> x + y)
                .subscribe(System.out::println);

    }

    @Test
    public void fluxFlatMap() throws InterruptedException {
        //flatMap按实际生产顺序进行合并
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(
                        Duration.ofMillis(x * 10),
                        Duration.ofMillis(100)).take(x)
                )
                .subscribe(System.out::println);
        Thread.sleep(2000);
        System.out.println("-------------");

//flatMapSequential按订阅顺序进行合并
        Flux.just(5, 10)
                .flatMapSequential(x -> Flux.interval(
                        Duration.ofMillis(x * 10),
                        Duration.ofMillis(100)).take(x)
                )
                .subscribe(System.out::println);
        Thread.sleep(1000);

    }

    @Test
    public void fluxConcatMap() throws InterruptedException {
        /**
         * concatMap会根据原始流中的元素顺序依次把转换之后的流进行合并，
         * 并且concatMap堆转换之后的流的订阅是动态进行的，
         * 而flatMapSequential在合并之前就已经订阅了所有的流。
         */
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(
                        Duration.ofMillis(x * 10),
                        Duration.ofMillis(100)).map(i-> i+"map1").take(x)
                )
                .subscribe(System.out::println);
        Thread.sleep(1000);

    }

    @Test
    public void fluxCombinLaest() throws InterruptedException {
        /**
         * combineLatest 把所有流中的最新产生的元素合并成一个新的元素
         * 把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
         * 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，
         * 结果流中就会产生新的元素。
         */
        Flux.combineLatest(Arrays::toString,
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .subscribe(System.out::println);
        Thread.sleep(1000);
    }
    @Test
    public void fluxSkip() throws InterruptedException {
        //跳过指定条数
        Flux.just(1,2,3,4,5,6,7)
                .skip(2)
                .subscribe(System.out::println);

//跳过指定时间间隔
        Flux.interval(Duration.ofMillis(100))
                .skip(Duration.ofMillis(300))
                .log()
                .subscribe();
        Thread.sleep(1000);

    }

    @Test
    public void fluxDistinct() {
        Flux.just(1,1,2,2,5,6,7)
                .distinct()
                .subscribe(System.out::println);

    }

    @Test
    public void getEntityFromFlux() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1,"jeffrey",24,"N",LocalDate.now(),"123456"));
        users.add(new User(2,"jeffrey2",24,"N",LocalDate.now(),"123456"));
        users.add(new User(3,"jeffrey3",24,"N",LocalDate.now(),"123456"));
        users.add(new User(4,"jeffrey4",24,"N",LocalDate.now(),"123456"));

        Flux<User> userFlux = Flux.fromIterable(users);
        Iterable<User> users1 = userFlux.toIterable();
        for (User user: users1
             ) {
            System.out.println(user);
        }

    }

}
