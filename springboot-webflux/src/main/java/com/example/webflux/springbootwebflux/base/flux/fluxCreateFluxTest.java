package com.example.webflux.springbootwebflux.base.flux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 17:43
 * @week 星期二
 * @description
 **/
public class fluxCreateFluxTest {

    /**
     * just(T... data)
     */
    @Test
    public void createTest01() {
        Flux.just(1, 2, 3).log().subscribe(System.out::println);
    }

    /**
     * from(Publisher<? extends T> source)
     */
    @Test
    public void createTest02() {
        Flux.from(Mono.just(1)).log().subscribe(System.out::println);
        Flux.from(Flux.just(3, 4, 5)).log().subscribe(System.out::println);
    }

    /**
     * fromArray(T[] array)
     */
    @Test
    public void createTest03() {
        String[] strings = new String[]{"a", "b", "c"};
        Flux.fromArray(strings).log().subscribe(System.out::println);
    }

    /**
     * fromIterable(Iterable<? extends T> it)
     */
    @Test
    public void createTest04() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("e", "d", "F"));
        Flux.fromIterable(strings).log().subscribe(System.out::println);
    }

    /**
     * fromStream(Stream<? extends T> s)
     */
    @Test
    public void createTest05() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("e", "d", "F"));
        Flux.fromStream(strings.stream()).log().subscribe(System.out::println);
    }


    /**
     * empty()
     * 创建一个通量，在不发射任何项目的情况下完成。
     */
    @Test
    public void createTest06() {
        Flux.empty().log().subscribe(System.out::println);
    }

    /**
     * error(Throwable error)
     * 创建一个Flux，在订阅后立即终止并出现指定的错误。
     */
    @Test
    public void createTest07() {
        Flux.error(new RuntimeException("error ...")).log().subscribe(System.out::println);
    }

    /**
     * range(int start, int count)
     * 构建一个Flux，从一开始只发出一个计数递增整数序列。
     */
    @Test
    public void createTest08() {
        Flux.range(1, 5).log().subscribe(System.out::println);
    }


    /**
     * never()
     * 创建一个通量，它永远不会发出任何数据、错误或完成信号。
     */
    @Test
    public void createTest09() {
        Flux.never().log().subscribe(System.out::println);
    }

    /**
     * interval(Duration period)
     * 创建一个通量，该通量发出从0开始的长值，并在全局计时器上以指定的时间间隔递增。
     */
    @Test
    public void createTest10() throws InterruptedException {
        Flux.just(1, 2).interval(Duration.ofMillis(10L)).log().subscribe(System.out::println);
        // 上面的1,2 是没有用的，还是从0开始
        TimeUnit.SECONDS.sleep(2);

        /**
         * 18:10:02.834 [parallel-1] INFO reactor.Flux.Interval.1 - onNext(0)
         * 0
         * 18:10:02.849 [parallel-1] INFO reactor.Flux.Interval.1 - onNext(1)
         * 1
         * 18:10:02.849 [parallel-1] INFO reactor.Flux.Interval.1 - onNext(2)
         * 2
         * 18:10:02.864 [parallel-1] INFO reactor.Flux.Interval.1 - onNext(3)
         * ...
         */
    }

    /**
     * create(Consumer<? super FluxSink<T>> emitter)
     * 参数 emitter 是一个用于发射元素的回调函数，它接收一个 FluxSink 对象作为参数。
     * 通过调用 FluxSink 对象的方法，我们可以发送元素、发送错误或者发送完成信号。
     */
    @Test
    public void createTest11() throws InterruptedException {
        Flux<Integer> flux = Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete();
        });

        flux.subscribe(System.out::println);  // 输出: 1 2 3
    }

    /**
     * create(Consumer<? super FluxSink<T>> emitter, FluxSink.OverflowStrategy backpressure)
     * 通过FluxSink API以同步或异步方式以编程方式创建具有发射多个元素能力的Flux。
     * <p>
     * 参数 emitter 是一个用于发射元素的回调函数，它接收一个 FluxSink 对象作为参数。
     * backpressure 参数是一个枚举类型，用于指定背压策略。背压策略决定了当订阅者无法及时处理元素时，应该如何处理溢出的情况。
     * FluxSink.OverflowStrategy 枚举提供了以下几种背压策略：
     * IGNORE：忽略溢出的元素，不做任何处理。
     * ERROR：当溢出发生时，立即抛出 IllegalStateException 异常。
     * BUFFER：使用一个缓冲区来存储溢出的元素，直到订阅者可以处理它们。
     * DROP：丢弃溢出的元素，不进行缓存。
     */
    @Test
    public void createTest12() throws InterruptedException {
        Flux<Integer> flux = Flux.create(sink -> {
            for (int i = 1; i <= 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.BUFFER);

        flux.subscribe(System.out::println);  // 输出: 1 2 3 4 5 6 7 8 9 10
    }

    /**
     * generate(Consumer<SynchronousSink<T>> generator)
     * 通过使用者回调逐个生成信号，以编程方式创建Flux。
     * <p>
     * 用于生成元素的回调函数，它接收一个 SynchronousSink 对象作为参数。
     * 通过调用 SynchronousSink 对象的方法，我们可以发送元素、发送错误或者发送完成信号。
     */
    @Test
    public void createTest13() throws InterruptedException {
        Flux<Integer> flux = Flux.generate(sink -> {
            sink.next(1);
            /*sink.next(2);
            sink.next(3);*/
            // 只能调用一次next
            sink.complete();
        });

        flux.subscribe(System.out::println);  // 输出: 1 2 3
        /**
         * Caused by: java.lang.IllegalStateException: More than one call to onNext
         * 	at reactor.core.publisher.FluxGenerate$GenerateSubscription.next(FluxGenerate.java:166)
         */

    }

    /**
     * generate(Callable<S> stateSupplier,
     * BiFunction<S,SynchronousSink<T>,S> generator)
     * 通过使用者回调和一些状态逐个生成信号，以编程方式创建Flux。
     * <p>
     * stateSupplier：用于提供初始状态的 Callable 对象。
     * generator：用于生成元素并更新状态的 BiFunction 对象，
     * 它接收当前状态和一个 SynchronousSink 对象作为参数，并返回更新后的状态。
     */
    @Test
    public void createTest14() throws InterruptedException {
        Flux<Integer> flux = Flux.generate(
                () -> 1,  // 初始状态
                (state, sink) -> {
                    sink.next(state);
                    if (state == 5) {
                        sink.complete();
                    }
                    return state + 1;  // 更新状态
                }
        );

        flux.subscribe(System.out::println);  // 输出: 1 2 3 4 5
    }


    /**
     * generate(Callable<S> stateSupplier,
     * BiFunction<S,SynchronousSink<T>,S> generator,
     * Consumer<? super S> stateConsumer)
     * <p>
     * 通过使用者回调和一些状态逐个生成信号，并通过最后的清理回调，以编程方式创建Flux。
     * <p>
     * stateSupplier：用于提供初始状态的 Callable 对象。
     * generator：用于生成元素并更新状态的 BiFunction 对象，
     * 它接收当前状态和一个 SynchronousSink 对象作为参数，并返回更新后的状态。
     * stateConsumer：用于消费最终状态的 Consumer 对象。
     */
    @Test
    public void createTest15() throws InterruptedException {
        Flux<Integer> flux = Flux.generate(
                () -> 1,  // 初始状态
                (state, sink) -> {
                    sink.next(state);
                    if (state == 5) {
                        sink.complete();
                    }
                    return state + 1;  // 更新状态
                },
                state -> System.out.println("Final state: " + state)
        );

        flux.subscribe(System.out::println);  // 输出: 1 2 3 4 5
    }


}
