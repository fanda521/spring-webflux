package com.example.webflux.springbootwebflux.base.mono;

import ch.qos.logback.core.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.example.webflux.springbootwebflux.base.mono.domain.FoodMono;
import com.example.webflux.springbootwebflux.base.mono.domain.FoodSonMono;
import com.example.webflux.springbootwebflux.base.mono.domain.OrderMono;
import com.example.webflux.springbootwebflux.base.mono.domain.ProductMono;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 12:26
 * @week 星期二
 * @description mono 中间过程的处理
 **/
public class ProcessMonoTest {

    /**
     * cast(Class<E> clazz)
     * 将当前Mono生成的类型转换为目标生成的类型。
     *
     * 不能将确定的class 转成其他的
     * json 格式的字符串也不行
     * 父类转子类不行
     *
     * 只有 子类转父类 可以
     */
    @Test
    public void processTest01() throws InterruptedException {
        ProductMono productMono = new ProductMono();
        productMono.setName("洗衣液");
        productMono.setId(1L);
        productMono.setPrice(new BigDecimal("22.3456"));
        productMono.setAddress(Arrays.asList("广东","江西","武汉"));


        /*Mono<ProductMono> just = Mono.just(productMono);
        Mono<FoodMono> foodMono = just.cast(FoodMono.class);
        foodMono.log().subscribe(System.out::println);*/

        /*String s = JSON.toJSONString(productMono);
        Mono<String> just = Mono.just(s);
        just.cast(ProductMono.class).log().subscribe(System.out::println);*/
        // 下面这种的有继承关系的才行
        /*Object o = new Object();
        o = productMono;
        Mono<Object> just = Mono.just(o);
        just.cast(ProductMono.class).log().subscribe(System.out::println);*/

        // ProductMono 是 FoodSonMono 的 父类
        // 结果不行 父类是无法转成子类的
        /*Mono<ProductMono> just = Mono.just(productMono);
        Mono<FoodSonMono> foodSonMono = just.cast(FoodSonMono.class);
        foodSonMono.log().subscribe(System.out::println);*/


        // ProductMono 是 FoodSonMono 的 父类
        // 子类转成父类 是可以的
        FoodSonMono foodSonMono = new FoodSonMono();
        foodSonMono.setName("洗衣液");
        foodSonMono.setId(1L);
        foodSonMono.setPrice(new BigDecimal("22.3456"));
        foodSonMono.setAddress(Arrays.asList("广东","江西","武汉"));
        foodSonMono.setTaste("美味香甜");
        foodSonMono.setWeight(new Double(23.44));

        Mono<FoodSonMono> just = Mono.just(foodSonMono);
        Mono<ProductMono> cast = just.cast(ProductMono.class);
        System.out.println("--------------------------------");
        cast.log().subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);

    }

    /**
     * concatWith(Publisher<? extends T> other)
     * 将此单声道的发射与所提供的发布服务器连接（无交错）。
     */
    @Test
    public void processTest02() {
        Mono<Integer> just = Mono.just(1);
        // 和 mono
        just.concatWith(Mono.just(2)).log().subscribe(System.out::println);
        // 和 flux
        just.concatWith(Flux.just(5, 6, 7)).log().subscribe(System.out::println);
    }

    /**
     * defaultIfEmpty(T defaultV)
     * 如果此单声道在没有任何数据的情况下完成，请提供默认的单个值
     */
    @Test
    public void processTest03() {
        Mono.just(1).filter(s -> s > 2).defaultIfEmpty(10).log().subscribe(System.out::println);
    }

    /**
     * expand(Function<? super T,? extends Publisher<? extends T>> expander)
     * 使用广度优先遍历策略将元素递归扩展到图中，并发出所有生成的元素。
     */
    @Test
    public void processTest04() {
        Mono<Integer> mono = Mono.just(2);
        Flux<Integer> expandedFlux = mono.expand(num -> {
            if (num <= 10) {
                return Mono.just(num + 1);
            } else {
                return Mono.empty();
            }
        });
        expandedFlux.subscribe(System.out::println);
    }

    /**
     * filter(Predicate<? super T> tester)
     * 如果这个Mono是有值的，那么测试结果并在谓词返回true时重播它。
     */
    @Test
    public void processTest05() {
        Mono<Integer> mono = Mono.just(2);
        mono.filter(s -> s > 2).log().subscribe(System.out::println);

    }

    /**
     * filterWhen(Function<? super T,? extends Publisher<Boolean>> asyncPredicate)
     * 如果此Mono有值，请使用生成的Publisher＜Boolean＞测试异步测试该值。
     */
    @Test
    public void processTest06() {
        Mono<Integer> mono = Mono.just(5);
        Mono<Integer> filteredMono = mono.filterWhen(num -> {
            return Mono.just(num % 2 == 0);
            // 注意这里的返回值是mono,准确的说是publisher 类型
        });
        filteredMono.subscribe(System.out::println);
    }

    /**
     * handle(BiConsumer<? super T,SynchronousSink<R>> handler)
     * 通过为每个onNext调用带有输出接收器的biconsumer来处理此Mono发出的项。
     * 接受一个 BiConsumer 参数，该参数接受两个参数：输入元素和 SynchronousSink。
     * 通过在 BiConsumer 中处理输入元素，并使用 SynchronousSink 发出结果，
     * 可以对 Mono 的元素进行处理。
     */
    @Test
    public void processTest07() {
        Mono<Integer> mono = Mono.just(5);
        Mono<String> handledMono = mono.handle((num, sink) -> {
            if (num % 2 == 0) {
                sink.next("Even");
            } else {
                sink.next("Odd");
            }
        });
        handledMono.subscribe(System.out::println);
    }

    /**
     * firstWithValue(Iterable<? extends Mono<? extends T>> monos)
     * 选择第一个发出任何值的Mono源，并重播该信号，有效地表现为第一个发出onNext的源。
     *
     * 用于从一组 Mono 对象中获取第一个发出值的 Mono。它接受一个 Iterable<? extends Mono<? extends T>> 参数，
     * 表示要检查的 Mono 对象的集合。如果集合中的任何 Mono 对象发出了值，则返回第一个发出值的 Mono；
     * 如果所有 Mono 对象都没有发出值，则返回一个空的 Mono。
     */
    @Test
    public void processTest08() {
        Mono<Integer> mono1 = Mono.just(1);
        Mono<Integer> mono2 = Mono.empty();
        Mono<Integer> mono3 = Mono.just(3);

        Mono.firstWithValue(Arrays.asList(mono1, mono2, mono3))
                .subscribe(value -> System.out.println("First mono with value: " + value),
                        (value) -> System.out.println("No mono with value found"));

        // 阻塞主线程，等待订阅完成
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 上述代码会输出 "First mono with value: 1"，因为在给定的 Mono 集合中，
         * 第一个发出值的 Mono 是 Mono.just(1)。
         * 如果将集合中的 Mono 对象都改为 Mono.empty()，则会输出 "No mono with value found"。
         */
    }

    /**
     * 	flatMap(Function<? super T,? extends Mono<? extends R>> transformer)
     * 	异步转换此Mono发出的项，返回另一个Mono发出（可能更改值类型）的值。
     */
    @Test
    public void processTest09() {
        Mono.just(2)
                .flatMap(value -> {
                    if (value % 2 == 0) {
                        return Mono.just(value * 2); // 返回一个新的 Mono 对象
                    } else {
                        return Mono.empty(); // 返回一个空的 Mono 对象
                    }
                })
                .subscribe(result -> System.out.println("Result: " + result),
                        (result) -> System.out.println("No result"));

        // 阻塞主线程，等待订阅完成
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * flatMapIterable(Function<? super T,? extends Iterable<? extends R>> mapper)
     * 将这个Mono发出的项目转换为Iterable，然后将其元素转发到返回的Flux中。
     *
     * 可以用来将 Mono 中的元素转换为一个可迭代对象，并将其展平为一个 Flux 对象。
     * 它的作用类似于 flatMap 操作符，但是可以处理可迭代对象作为返回结果。
     */
    @Test
    public void processTest10() {
        Mono<String> mono = Mono.just("Hello");
        Flux<String> stringFlux = mono.flatMapIterable(s -> Arrays.asList(s.split("")));
        stringFlux.subscribe(System.out::println);
    }



    /**
     * flatMapMany(Function<? super T,? extends Publisher<? extends R>> mapper)
     * 将此Mono发射的项目转换为发布器，然后将其发射转发到返回的Flux中。
     *
     * 可以用来将 Mono 中的元素转换为一个 Flux 对象，并将其展平为一个新的 Flux 对象。
     * 它的作用类似于 flatMap 操作符，但是可以处理 Flux 对象作为返回结果。
     *
     */
    @Test
    public void processTest11() {
        // 1.简单版
        /*Mono<List<Integer>> mono = Mono.just(Arrays.asList(1, 2, 3));

        Flux<Integer> result = mono.flatMapMany(Flux::fromIterable)
                .map(i -> i * 2);

        result.subscribe(System.out::println);*/

        // 2.复杂版
        Mono<OrderMono> mono = Mono.just(new OrderMono("123", Arrays.asList("Apple", "Banana", "Orange")));

        /*Flux<OrderMono> orderMonoFlux = mono.flatMapMany(order -> Flux.just(order));
        orderMonoFlux.log().subscribe(System.out::println);*/

        Flux<String> result = mono.flatMapMany(order -> Flux.fromIterable(order.getItems()))
                .flatMap(item -> Flux.fromIterable(splitItem(item)));
        result.subscribe(System.out::println);

    }

    private static List<String> splitItem(String item) {
        List<String> result = new ArrayList<>();
        for (char c : item.toCharArray()) {
            result.add(String.valueOf(c));
        }
        return result;
    }

    /**
     * 	mapNotNull(Function<? super T,? extends R> mapper)
     * 	通过对该Mono发出的项应用同步函数来转换该项，该函数允许生成null值。
     *
     * 	可以用于将 Mono 中的元素应用于一个转换函数，并过滤掉转换结果为 null 的元素，返回一个新的 Mono 对象。
     */
    @Test
    public void processTest12() {
        Mono<String> mono = Mono.just("Hello")
                .mapNotNull(s -> {
                    if (s.length() > 4) {
                        return s.toUpperCase();
                    } else {
                        return null;
                    }
                });

        mono.subscribe(System.out::println);
    }


    /**
     * name(String name)
     * 为该序列指定一个名称，只要这是第一个可访问的Scannableparents（），就可以使用Scannable.name（）检索该序列。
     *
     * 通过使用 name 方法，你可以为 Mono 对象设置一个可识别的名称，以便在调试和跟踪时进行标识。
     * 这有助于在复杂的代码中区分和追踪不同的 Mono 对象。
     */
    @Test
    public void processTest13() {
        Mono.just(1).name("ss").log().subscribe(System.out::println);
    }

    /**
     * mergeWith(Publisher<? extends T> other)
     * Merge emissions of this Mono with the provided Publisher.
     *
     * 用于将当前 Mono 对象与另一个 Mono 对象进行合并。合并后的结果将作为新的 Flux 对象返回。
     */

    @Test
    public void processTest14() {
        Mono<String> mono1 = Mono.just("Hello");
        Mono<String> mono2 = Mono.just("World");

        Flux<String> stringFlux = mono1.mergeWith(mono2);

        stringFlux.subscribe(System.out::println);
    }

    /**
     * ofType(Class<U> clazz)
     * 根据给定的类类型计算发射的值。
     */
    @Test
    public void processTest15() {
        Mono<Object> just = Mono.just(1);
        Mono<String> two = Mono.just("two");
        Flux<String> result = just.concatWith(two)
                .concatWith(Mono.just(3))
                .concatWith(Mono.just("four"))
                .concatWith(Mono.just(5)).ofType(String.class);
        result.subscribe(System.out::println);
    }


    /**
     * single()
     *
     Expect exactly one item from this Mono source or signal NoSuchElementException for an empty source.
     期望此Mono源中正好有一个项，或者对于空源发出NoSuchElementException信号。
     */
    @Test
    public void processTest16() {
        Mono<Integer> mono = Mono.just(42);

        mono.single()
                .subscribe(System.out::println);

        //Mono.empty().single().subscribe(System.out::println);
        // 上面的空对象会报错

        Mono.empty().singleOptional().subscribe(System.out::println);
    }

    /**
     * singleOptional()
     * 将此Mono源生成的项包装为Optional，或者为空源发出空的Optional。
     */
    @Test
    public void processTest17() {
        Mono<Integer> mono = Mono.just(42);

        mono.singleOptional()
                .subscribe(System.out::println);

        Mono.empty().singleOptional().subscribe(System.out::println);

        /**
         * Optional[42]
         * Optional.empty
         */
    }


    /**
     * switchIfEmpty(Mono<? extends T> alternate)
     * 如果此单声道在没有数据的情况下完成，则回退到另一个单声道
     *
     * 用于在源Mono为空时，切换到一个备用的Mono对象。
     */
    @Test
    public void processTest18() {
        Mono<Integer> mono = Mono.empty();

        Mono<Integer> alternate = Mono.just(42);

        mono.switchIfEmpty(alternate)
                .subscribe(System.out::println);
    }


    /**
     * take(Duration duration)
     * 给这个Mono一个在指定的时间范围内解决的机会，但如果没有，就完成。
     */
    @Test
    public void processTest19() {
        Mono.just(1)
                .delayElement(Duration.ofSeconds(1))
                .concatWith(Mono.just(2))
                .concatWith(Mono.just(3))
                .concatWith(Mono.just(4)
                        .delayElement(Duration.ofSeconds(1)))
                .concatWith(Mono.just(5).delayElement(Duration.ofSeconds(4)))
                .take(Duration.ofSeconds(3))
                .subscribe(System.out::println);

        Mono.just(11).take(Duration.ofSeconds(1)).subscribe(System.out::println);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**
         * 11
         * 1
         * 2
         * 3
         * 4
         */
    }

    /**
     * then()
     * 返回一个单声道＜Void＞，该单声道只回放来自该单声道的完整信号和错误信号。
     *
     * then方法用于在源Mono完成后执行一个操作。它返回一个新的Mono对象，可以继续进行其他操作。
     *
     * then(Mono<V> other)
     * Let this Mono complete then play another Mono.
     * 让这个单声道完成，然后播放另一个单声道。
     */
    @Test
    public void processTest20() {
        Mono.just(42).log().map(s -> s + 2).then(Mono.just(60)).log()
                .single().map(s -> s + 3)
                .doOnSuccess(v -> System.out.println("Completion"))
                .subscribe(System.out::println);
    }


    /**
     * thenEmpty(Publisher<Void> other)
     * 返回一个Mono＜Void＞，等待此Mono完成，然后等待提供的Publisher＜Void〕也完成。
     *
     * 用于在源Mono完成后执行另一个Publisher对象，并返回一个空的Mono。
     */
    @Test
    public void processTest21() throws InterruptedException {
        MonoProcessor<Integer> mono = MonoProcessor.create();

        Mono<Void> otherMono = Mono.empty();
        otherMono.handle((value , sink) ->{
            try {
                System.out.println("otherMono enter");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("otherMono outer");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sink.next(1);
            System.out.println("sink complete");
        });

        mono.thenEmpty(otherMono)
                .doOnSuccess(v -> System.out.println("Completion"))
                .subscribe(System.out::println);

        mono.onNext(42);
        mono.onComplete();

        Thread.sleep(4000);
    }

    /**
     * thenMany(Publisher<V> other)
     * 让这个Mono成功完成，然后播放另一个Publisher
     *
     * 用于在源Mono完成后执行另一个Publisher对象，并返回该Publisher对象的结果。
     */
    @Test
    public void processTest22() throws InterruptedException {
        Mono<Integer> mono = Mono.just(42);

        Flux<String> otherFlux = Flux.just("a", "b", "c");

        mono.thenMany(otherFlux)
                .subscribe(System.out::println);
        /**
         * a
         * b
         * c
         */
    }

    /**
     * thenReturn(V value)
     * Let this Mono complete successfully, then emit the provided value.
     * 让这个Mono成功完成，然后发出所提供的值。
     *
     * 用于在源Mono完成后返回一个指定的值。
     */

    @Test
    public void processTest23() {
        Mono<Integer> mono = Mono.just(42);

        mono.thenReturn(sum(mono.block()))
                .subscribe(System.out::println);
        /**
         * 1764
         */
    }

    private Integer sum (Integer data) {
        return  data * data;
    }

    /**
     * timed()
     * Times this Mono Subscriber.onNext（Object）事件，封装到一个Timed对象中，
     * 该对象允许下游消费者使用默认时钟（Scheduler.parallel（））
     * 查看以纳秒分辨率收集的各种时间信息：Timed.elapsed（）：
     * 自订阅以来以纳秒为单位的时间，作为Duration。
     *
     * 用于在源Mono发出元素时，将元素包装为Timed对象，该对象包含元素值以及相对于订阅开始的时间信息。
     */

    @Test
    public void processTest24() {
        Mono<Integer> mono = Mono.just(42);

        mono.timed()
                .subscribe(timed -> {
                    long elapsedTime = timed.elapsed().toMillis();
                    int value = timed.get();
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Value: " + value);
                    System.out.println("Elapsed Time (ms): " + elapsedTime);
                });
    }

    /**
     * 	timeout(Duration timeout)
     * 	如果在给定的持续时间内没有项目到达，则传播TimeoutException。
     *
     *
     */

    @Test
    public void processTest25() {
        Mono<Integer> mono = Mono.just(42)
                .delayElement(Duration.ofSeconds(5));

        mono.timeout(Duration.ofSeconds(3))
                .subscribe(System.out::println, error -> {
                    System.out.println("Timeout Exception: " + error.getMessage());
                });

        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /**
         * Timeout Exception: Did not observe any item or terminal signal
         * within 3000ms in 'delayElement' (and no fallback has been configured)
         */
    }

    /**
     * using(Callable<? extends D> resourceSupplier,
     *       Function<? super D,? extends Mono<? extends T>> sourceSupplier,
     *       Consumer<? super D> resourceCleanup)
     *
     *       resourceSupplier是一个Callable，用于创建和提供资源；
     *       monoSupplier是一个函数，接收资源并返回一个Mono对象；
     *       resourceCleanup是一个消费者函数，用于在完成时释放资源。
     *
     * 使用由供应商为每个订阅服务器生成的资源，同时流式传输来自同一资源的Mono的值，
     * 并确保在序列终止或订阅服务器取消时释放该资源
     */
    @Test
    public void processTest26() {
        Mono<String> mono = Mono.using(
                () -> acquireResource(),
                resource -> processResource(resource),
                resource -> releaseResource(resource)
        );

        mono.subscribe(System.out::println);
    }

    private static String acquireResource() {
        System.out.println("Acquiring resource");
        return "Resource";
    }

    private static Mono<String> processResource(String resource) {
        System.out.println("Processing resource: " + resource);
        return Mono.just(resource.toUpperCase());
    }

    private static void releaseResource(String resource) {
        System.out.println("Releasing resource: " + resource);
    }

    /**
     * when(Iterable<? extends Publisher<?>> sources)
     * 将给定的发布者聚合为一个新的Mono，当所有给定的发布器都完成时，该Mono就会完成。
     *
     * 用于等待多个Mono对象都完成后，将它们的结果组合成一个新的Mono对象。
     */

    @Test
    public void processTest27() throws InterruptedException {
        Mono<String> mono1 = Mono.just("Hello").delayElement(Duration.ofSeconds(1));
        Mono<String> mono2 = Mono.just("World").delayElement(Duration.ofSeconds(3));
        Mono.when(mono1, mono2);
        mono1.subscribe(System.out::println);
        mono2.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(4);

    }

    /**
     * whenDelayError(Iterable<? extends Publisher<?>> sources)
     * 将给定的发布者聚合为一个新的Mono，当所有给定的源都完成时，该Mono将被实现。
     *
     * 用于等待多个Mono对象都完成后，将它们的结果组合成一个新的Mono对象。
     * 与when方法不同的是，whenDelayError方法会延迟抛出异常，直到所有参与的Mono都完成。
     */

    @Test
    public void processTest28() throws InterruptedException {

        Mono<String> mono1 = Mono.just("Hello")
                .delayElement(Duration.ofSeconds(2))
                .map(value -> {
                    throw new RuntimeException("error mono1");
                });
        Mono<String> mono2 = Mono.just("World");

        Mono<Void> combinedMono = Mono.whenDelayError(mono1, mono2)
                .then();

        combinedMono.subscribe(
                null,
                error -> System.out.println("Error: " + error.getMessage())
        );

        TimeUnit.SECONDS.sleep(4);
    }

    /**
     * zip(Mono<? extends T1> p1, Mono<? extends T2> p2)
     * 将给定的Mono合并到一个新的Mono中，当所有给定的monos都生成了一个项目时，
     * 该项目将被实现，并将它们的值聚合到一个Tuple2中
     */
    @Test
    public void processTest29() throws InterruptedException {
        Mono<String> mono1 = Mono.just("Hello");
        Mono<String> mono2 = Mono.just("World");

        /*Mono<String> combinedMono = Mono.zip(mono1, mono2, (result1, result2) -> result1 + " " + result2);
        combinedMono.subscribe(System.out::println);*/

        Mono<Tuple2<String, String>> zip = Mono.zip(mono1, mono2);
        zip.subscribe(current -> {
            System.out.println(current.getT1() + " - " + current.getT2());
        });

    }

    /**
     * zipWhen(Function<T,Mono<? extends T2>> rightGenerator)
     * 等待这个单声道的结果，使用它通过提供的rightGenerator函数创建第二个单声道，
     * 并将两个结果组合成Tuple2。
     *
     * otherFactory：用于生成第二个 Mono 序列的函数，该函数接收第一个 Mono 的结果作为输入，
     * 并返回一个 Publisher（可以是 Mono、Flux 等）。
     *
     * combinator：用于将第一个 Mono 和第二个 Mono 的结果进行合并的函数，
     * 该函数接收第一个 Mono 和第二个 Mono 的结果作为输入，并返回一个新的结果。
     */

    @Test
    public void processTest30() throws InterruptedException {
        Mono<Integer> mono1 = Mono.just(1);
        Mono<String> mono2 = Mono.just("A");

        Mono<String> result = mono1.zipWhen(i -> mono2 , (i, s) -> i + s);
        result.subscribe(System.out::println);  // 输出 "1A"

    }

    /**
     * zipWith(Mono<? extends T2> other, BiFunction<? super T,? super T2,? extends O> combinator)
     * 将这个mono和另一个mono的结果组合成任意O对象，如所提供的组合子函数所定义的。
     *
     * 用于将两个 Mono 序列进行合并，并通过一个函数将它们的结果进行组合。
     * other：要合并的第二个 Mono 序列。
     * combinator：用于将第一个 Mono 和第二个 Mono 的结果进行合并的函数，
     * 该函数接收第一个 Mono 和第二个 Mono 的结果作为输入，并返回一个新的结果。
     */

    @Test
    public void processTest31() throws InterruptedException {
        Mono<Integer> mono1 = Mono.just(1);
        Mono<String> mono2 = Mono.just("A");

        Mono<String> result = mono1.zipWith(mono2, (i, s) -> i + s);
        result.subscribe(System.out::println);  // 输出 "1A"
    }



}
