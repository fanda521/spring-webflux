package com.example.webflux.springbootwebflux.base.flux;

import com.example.webflux.springbootwebflux.base.flux.domain.Book;
import com.example.webflux.springbootwebflux.base.flux.domain.CustomObjectFlux;
import com.example.webflux.springbootwebflux.base.flux.domain.Order;
import com.example.webflux.springbootwebflux.base.flux.domain.OrderItem;
import com.example.webflux.springbootwebflux.base.flux.domain.PersonFlux;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
     * <p>
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
     * <p>
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

    /**
     * blockFirst()
     * 订阅此Flux并无限期阻止，直到上游发出其第一个值的信号或完成。
     * <p>
     * 用于阻塞地获取 Flux 序列的第一个元素，并返回该元素的值。
     */
    @Test
    public void processTest03() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

        Integer firstElement = flux.blockFirst();
        System.out.println(firstElement);  // 输出: 1

        Flux<Integer> flux2 = Flux.empty();

        Integer firstElement2 = flux2.blockFirst();
        System.out.println(firstElement2);  // 输出: null
    }

    /**
     * buffer()
     * 将所有传入的值收集到单个List缓冲区中，该缓冲区将在该Flux完成后由返回的Flux发出。
     * <p>
     * 用于将 Flux 序列中的元素按照指定的规则进行分组，
     * 然后将每个分组的元素作为一个集合放入新的 Flux 序列中。
     */
    @Test
    public void processTest04() {
        Flux<Integer> flux = Flux.range(1, 10);
        Flux<List<Integer>> bufferedFlux = flux.buffer();
        bufferedFlux.subscribe(System.out::println);  // 输出: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

    }

    /**
     * buffer(Duration bufferingTimespan,
     * Duration openBufferEvery,
     * Scheduler timer)
     * 将传入值收集到在给定openBufferEvery周期创建的多个List缓冲区中，如在提供的Scheduler上测量的那样
     * <p>
     * 可以根据时间窗口和间隔时间将 Flux 序列中的元素分组为集合。
     * bufferingTimespan：时间窗口的持续时间，用于确定每个分组的时间范围。
     * openBufferEvery：分组的间隔时间，用于确定何时开始新的分组。
     * timer：用于定时的调度器。可选参数，如果不指定，则使用默认的调度器
     */
    @Test
    public void processTest05() throws InterruptedException {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(100));

        Scheduler scheduler = Schedulers.parallel();
        Flux<List<Long>> bufferedFlux = flux.buffer(Duration.ofSeconds(1), Duration.ofMillis(500), scheduler);
        bufferedFlux.subscribe(System.out::println);  // 输出: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9], [10, 11, 12, 13, 14, 15, 16, 17, 18, 19], ...


        Flux<Long> flux2 = Flux.interval(Duration.ofMillis(100));

        Flux<List<Long>> bufferedFlux2 = flux2.buffer(Duration.ofSeconds(1), Duration.ofMillis(500));
        bufferedFlux2.subscribe(System.out::println);  // 输出: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9], [10, 11, 12, 13, 14, 15, 16, 17, 18, 19], ...

        Thread.sleep(10 * 1000);
        /**
         * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
         * [5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
         * [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
         * [15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
         * [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
         * [25, 26, 27, 28, 29, 30, 31, 32, 33, 34]
         * [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
         * [35, 36, 37, 38, 39, 40, 41, 42, 43, 44]
         * [40, 41, 42, 43, 44, 45, 46, 47, 48, 49]
         * [45, 46, 47, 48, 49, 50, 51, 52, 53, 54]
         * [50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
         * [55, 56, 57, 58, 59, 60, 61, 62, 63, 64]
         * [60, 61, 62, 63, 64, 65, 66, 67, 68, 69]
         * [65, 66, 67, 68, 69, 70, 71, 72, 73, 74]
         * [70, 71, 72, 73, 74, 75, 76, 77, 78, 79]
         * [75, 76, 77, 78, 79, 80, 81, 82, 83, 84]
         * [80, 81, 82, 83, 84, 85, 86, 87, 88, 89]
         * [85, 86, 87, 88, 89, 90, 91, 92, 93, 94]
         */

    }

    /**
     * buffer(int maxSize, int skip, Supplier<C> bufferSupplier)
     * 将传入值收集到多个用户定义的“收集”缓冲区中，每次达到给定的最大大小或此Flux完成时，
     * 返回的Flux都会发出这些缓冲区。
     * <p>
     * 可以根据元素数量来分组 Flux 序列，并可以自定义缓冲区的类型。
     * maxSize：每个分组的最大元素数量。
     * skip：每个分组之间跳过的元素数量。
     * bufferSupplier：用于创建缓冲区的供应商函数，返回一个实现了 Collection 接口的集合。
     */
    @Test
    public void processTest06() {
        Flux<Integer> flux = Flux.range(1, 10);
        Flux<List<Integer>> bufferedFlux = flux.buffer(3, 2, ArrayList::new);
        bufferedFlux.subscribe(System.out::println);  // 输出: [1, 2, 3], [3, 4, 5], [5, 6, 7], [7, 8, 9], [9, 10]
        /**
         * [1, 2, 3]
         * [3, 4, 5]
         * [5, 6, 7]
         * [7, 8, 9]
         * [9, 10]
         */
    }

    /**
     * bufferTimeout(int maxSize,
     * Duration maxTime,
     * Scheduler timer,
     * Supplier<C> bufferSupplier)
     * 将传入值收集到多个用户定义的“收集”缓冲区中，
     * 这些缓冲区将在每次缓冲区达到最大大小或
     * 经过最大持续时间时由返回的Flux发出，
     * 如在提供的Scheduler上测量的那样。
     * <p>
     * 可以根据元素数量和时间窗口来分组 Flux 序列，并可以自定义缓冲区的类型。
     * maxSize：每个分组的最大元素数量。
     * maxTime：每个分组的时间窗口，超过时间窗口后会自动触发分组。
     * timer：用于定时的调度器。
     * bufferSupplier：用于创建缓冲区的供应商函数，返回一个实现了 Collection 接口的集合。
     */
    @Test
    public void processTest07() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 10).delayElements(Duration.ofMillis(200));

        Flux<List<Integer>> bufferedFlux = flux.bufferTimeout(3, Duration.ofSeconds(1), Schedulers.parallel(), ArrayList::new);
        bufferedFlux.subscribe(System.out::println);  // 输出: [1, 2, 3], [4, 5, 6], [7, 8, 9], [10]

        Thread.sleep(3 * 1000);

    }

    /**
     * bufferUntil(Predicate<? super T> predicate, boolean cutBefore)
     * 将传入值收集到多个List缓冲区中，这些缓冲区将在每次给定谓词返回true时由结果Flux发出。
     * <p>
     * 用于根据给定的条件对 Flux 序列中的元素进行分组，
     * 并将每个分组的元素作为一个集合放入新的 Flux 序列中。
     * <p>
     * predicate：用于判断是否要结束当前分组的条件。当条件满足时，会结束当前分组并开始新的分组。
     * cutBefore：是否在满足条件的元素之前结束当前分组。
     * 若为 true，则将满足条件的元素放入下一个分组；
     * 若为 false，则将满足条件的元素放入当前分组。
     */
    @Test
    public void processTest08() throws InterruptedException {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Flux<List<Integer>> bufferedFlux = flux.bufferUntil(num -> num % 3 == 0, false);
        bufferedFlux.subscribe(System.out::println);

        /**
         * true
         * [1, 2]
         * [3, 4, 5]
         * [6, 7, 8]
         * [9, 10]
         *
         * false
         * [1, 2, 3]
         * [4, 5, 6]
         * [7, 8, 9]
         * [10]
         */
    }

    /**
     * checkpoint(String description, boolean forceStackTrace)
     * 激活回溯（完整部件跟踪或较轻的部件标记，具体取决于forceStackTrace选项）。
     * <p>
     * 用于在 Flux 序列中的某个位置添加一个检查点（checkpoint），
     * 以便在出现错误时提供更详细的错误信息。
     * description：检查点的描述信息，用于标识检查点的位置。
     * forceStackTrace：是否强制生成详细的堆栈跟踪信息。
     * 若为 true，则在错误信息中包含完整的堆栈跟踪；
     * 若为 false，则只包含简要的错误信息。
     */
    @Test
    public void processTest09() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 5)
                .map(num -> {
                    if (num == 3) {
                        throw new RuntimeException("Error occurred at num = 3");
                    }
                    return num;
                })
                .checkpoint("Error Checkpoint", true);
        flux.subscribe(System.out::println);
    }

    /**
     * collect(Collector<? super T,A,? extends R> collector)
     * 通过应用Java 8流API收集器，将此Flux发出的所有元素收集到一个容器中。
     * 当此序列完成时，将发出收集的结果，如果序列为空，则发出空容器。
     * <p>
     * 用于将 Flux 序列中的元素收集到一个容器中，
     * 并将容器作为一个新的 Mono 序列进行返回。
     */
    @Test
    public void processTest10() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 5);

        Mono<List<Integer>> collectedMono = flux.collect(Collectors.toList());
        collectedMono.subscribe(System.out::println);  // 输出: [1, 2, 3, 4, 5]
    }

    /**
     * collect(Supplier<E> containerSupplier, BiConsumer<E,? super T> collector)
     * 通过应用收集器BiConsumer获取容器和每个元素，将此Flux发出的所有元素收集到用户定义的容器中。
     * <p>
     * containerSupplier：一个供应商函数，用于创建一个容器对象。
     * collector：一个双消费者函数，用于将元素收集到容器中。
     */
    @Test
    public void processTest11() throws InterruptedException {
        // 简单版
        Flux<Integer> flux = Flux.range(1, 10);
        HashMap<Object, Object> collectedMap = flux.collect(
                HashMap::new,
                (map, num) -> {
                    if (num % 2 == 0) {
                        map.put(num, num * num);
                    }
                }
        ).block();
        System.out.println(collectedMap);

        // 复杂版
        Flux<Integer> flux2 = Flux.just(-1, 2, -3, 4, -5, 6);

        CustomObjectFlux collectedObject = flux2.collect(
                CustomObjectFlux::new,
                (obj, num) -> {
                    if (num < 0) {
                        obj.addNegative(num);
                    } else {
                        obj.addPositive(num);
                    }
                }
        ).block();

        System.out.println("Positives: " + collectedObject.getPositives());
        System.out.println("Negatives: " + collectedObject.getNegatives());
    }

    /**
     * collectList()
     * 将此Flux发射的所有元素收集到一个List中，该List在该序列完成时由生成的Mono发射，
     * 如果该序列为空，则发射空List。
     */
    @Test
    public void processTest12() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 5);

        Mono<List<Integer>> collectedMono = flux.collectList();
        collectedMono.subscribe(System.out::println);  // 输出: [1, 2, 3, 4, 5]

    }

    /**
     * collectMap(Function<? super T,? extends K> keyExtractor)
     * 将该Flux发射的所有元素收集到哈希映射中，该哈希映射在该序列完成时由生成的Mono发射，
     * 如果序列为空，则发射空的Map
     * <p>
     * 用于将 Flux 序列中的元素收集到一个 Map 中，其中元素的键由给定的函数提取，值为元素本身。
     */
    @Test
    public void processTest13() throws InterruptedException {
        Flux<String> flux = Flux.just("apple", "banana11", "cherry");

        Mono<Map<Integer, String>> collectedMono = flux.collectMap(String::length);
        collectedMono.subscribe(System.out::println);  // 输出: {5=apple, 8=banana, 6=cherry}
    }

    /**
     * collectMap(Function<? super T,? extends K> keyExtractor,
     * Function<? super T,? extends V> valueExtractor)
     * 将该Flux发射的所有元素收集到哈希映射中，该哈希映射在该序列完成时由生成的Mono发射，
     * 如果序列为空，则发射空的Map。
     * <p>
     * 允许根据指定的键提取函数和值提取函数，将 Flux 序列中的元素收集到一个 Map 中。
     * keyExtractor：一个函数，用于从元素中提取键。
     * valueExtractor：一个函数，用于从元素中提取值。
     */
    @Test
    public void processTest14() throws InterruptedException {
        Flux<String> flux = Flux.just("apple", "banana11", "cherry");

        Mono<Map<Integer, String>> collectedMono = flux.collectMap(String::length, String::toUpperCase);
        collectedMono.subscribe(System.out::println);  // 输出: {5=APPLE, 6=CHERRY, 8=BANANA11}
    }

    /**
     * collectMap(Function<? super T,? extends K> keyExtractor,
     * Function<? super T,? extends V> valueExtractor,
     * Supplier<Map<K,V>> mapSupplier)
     * <p>
     * 将该通量发射的所有元素收集到用户定义的Map中，该Map在该序列完成时由生成的Mono发射，
     * 如果序列为空，则发射空Map。
     * <p>
     * 允许自定义的 Map 容器类型，并根据指定的键提取函数和值提取函数，
     * 将 Flux 序列中的元素收集到该容器中。
     * keyExtractor：一个函数，用于从元素中提取键。
     * valueExtractor：一个函数，用于从元素中提取值。
     * mapSupplier：一个供应商函数，用于创建一个自定义的 Map 容器对象。
     */
    @Test
    public void processTest15() throws InterruptedException {
        Flux<PersonFlux> flux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 35)
        );

        Supplier<Map<String, Integer>> mapSupplier = HashMap::new;
        Mono<Map<String, Integer>> collectedMono = flux.collectMap(PersonFlux::getName, PersonFlux::getAge, mapSupplier);
        collectedMono.subscribe(System.out::println);  // 输出: {Alice=25, Bob=30, Charlie=35}
    }

    /**
     * collectMultimap(Function<? super T,? extends K> keyExtractor,
     * Function<? super T,? extends V> valueExtractor,
     * Supplier<Map<K,Collection<V>>> mapSupplier)
     * <p>
     * 将此Flux发射的所有元素收集到用户定义的多映射中，该多映射在该序列完成时由生成的Mono发射，
     * 如果序列为空，则发射空的多映射。
     * <p>
     * 用于根据指定的键提取函数和值提取函数，将 Flux 序列中的元素收集到一个多值 Map 中。
     * keyExtractor：一个函数，用于从元素中提取键。
     * valueExtractor：一个函数，用于从元素中提取值。
     * mapSupplier：一个供应商函数，用于创建一个自定义的 Map 容器对象，其中值是一个集合
     */
    @Test
    public void processTest16() throws InterruptedException {
        Flux<PersonFlux> flux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 35)
        );

        Supplier<Map<String, Collection<Integer>>> mapSupplier = HashMap::new;
        Mono<Map<String, Collection<Integer>>> collectedMono = flux.collectMultimap(PersonFlux::getName, PersonFlux::getAge, mapSupplier);
        collectedMono.subscribe(System.out::println);
    }

    /**
     * collectSortedList()
     * 收集该Flux发射的所有元素，直到该序列完成，
     * 然后按自然顺序将它们排序到由生成的Mono发射的List中
     * <p>
     * collectSortedList(Comparator<? super T> comparator)
     */
    @Test
    public void processTest17() throws InterruptedException {
        Flux<PersonFlux> flux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );

        // 按照年龄升序对对象进行排序收集
        Mono<List<PersonFlux>> sortedList = flux.collectSortedList(Comparator.comparing(PersonFlux::getAge));

        sortedList.subscribe(list -> {
            for (PersonFlux person : list) {
                System.out.println(person);
            }
        });
    }

    /**
     * combineLatest(Function<Object[],V> combinator,
     * int prefetch,
     * Publisher<? extends T>... sources)
     * <p>
     * 构建一个Flux，其数据由每个Publisher源中最近发布的值的组合生成。
     * - `combinator`：一个函数，用于将最新的元素组合成一个新的值。
     * 该函数的参数是一个对象数组，数组中的元素对应于每个源Flux流的最新元素。函数的返回值类型为`v`。
     * - `prefetch`：一个整数值，表示在订阅时要请求的元素数量。
     * - `sources`：一个`Publisher`类型的参数，表示要组合的源Flux流。
     */
    @Test
    public void processTest18() throws InterruptedException {
        Flux flux1 = Flux.just(1, 2, 3, 4, 5);
        Flux flux2 = Flux.just(10, 20, 30, 40, 50);

        Flux.combineLatest(
                (objects) -> (int) objects[0] + (int) objects[1], // 组合器函数，将两个元素相加
                1, // 请求一个元素
                flux1, flux2
        ).subscribe(System.out::println);

        Thread.sleep(1000);
        /**
         * 15
         * 25
         * 35
         * 45
         * 55
         */
    }

    /**
     * concat(Publisher<? extends Publisher<? extends T>> sources)
     * 连接作为onNext信号从父发布服务器发出的所有源，将源发出的元素转发到下游。
     */
    @Test
    public void processTest19() throws InterruptedException {
        Mono<String> mono1 = Mono.just("Hello");
        Mono<String> mono2 = Mono.just("Reactor");
        Mono<String> mono3 = Mono.just("World");

        Flux<String> resultFlux = Flux.concat(mono1, mono2, mono3);

        resultFlux.subscribe(System.out::println);
    }

    /**
     * concat(Publisher<? extends Publisher<? extends T>> sources, int prefetch)
     * sources：要连接的多个发布者，类型为 Publisher<? extends Publisher<? extends T>>。
     * 它可以是一个 Flux、一个 Mono 的集合，或者是其他实现了 Publisher 接口的对象。
     * prefetch：预取数量，表示每个内部发布者（inner publisher）请求的项目数。
     * 这个参数可以用来提高性能，默认值为 Flux#SMALL_BUFFER_SIZE。
     */
    @Test
    public void processTest20() throws InterruptedException {
        Flux flux1 = Flux.just("Alice", "Smith", "Johnson");
        Flux.concat(flux1, 2).subscribe(System.out::println);
        /**
         * 测试没有通过
         * reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.ClassCastException:
         * class java.lang.String cannot be cast to class org.reactivestreams.Publisher
         * (java.lang.String is in module java.base of loader 'bootstrap';
         * org.reactivestreams.Publisher is in unnamed module of loader 'app')
         */
    }

    /**
     * concatMap(Function<? super T,? extends Publisher<? extends V>> mapper)
     * 将此Flux发出的元素异步转换为发布器，然后将这些内部发布器扁平化为单个Flux，依次使用串联来保持顺序。
     * <p>
     * 这个方法接受一个函数 mapper，该函数将每个源元素类型 T 转换为一个 Publisher 对象，并返回一个新的 Flux 对象。
     * concatMap() 方法会将这些转换后的 Publisher 对象连接在一起，确保它们的顺序是按照源 Flux 中的顺序进行的。
     * <p>
     * 它适用于以下场景：
     * <p>
     * 1.顺序执行：当需要按顺序处理源 Flux 中的元素，并确保每个元素的处理顺序与源顺序一致时，
     * 可以使用 concatMap() 方法。
     * <p>
     * 2.依赖关系：如果每个元素的处理依赖于前一个元素的处理结果，可以使用 concatMap() 方法。
     * 这样可以确保每个元素的处理在前一个元素处理完成后才进行。
     * <p>
     * 3.有序转换：如果需要将每个元素转换为一个新的 Publisher 对象，并且希望结果 Flux
     * 中的元素顺序与源 Flux 中的顺序一致，可以使用 concatMap() 方法。
     * <p>
     * 4.串行化操作：当需要将并发的操作转换为串行化的操作时，可以使用 concatMap() 方法。
     * 它确保了每个元素的处理在前一个元素处理完成后才开始，从而实现了串行化。
     */
    @Test
    public void processTest21() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );
        personFlux.subscribe(System.out::println);

        personFlux
                .concatMap(person -> Flux.just(person.getName()))
                .subscribe(System.out::println);
    }

    /**
     * concatMapDelayError(Function<? super T,? extends Publisher<? extends V>> mapper)
     * 将此Flux发出的元素异步转换为发布器，然后将这些内部发布器扁平化为单个Flux，依次使用串联来保持顺序。
     * <p>
     * concatMapDelayError() 方法在源 Flux 或转换后的 Publisher 中发生错误时，
     * 会将错误推迟到整个流程完成后再处理。这意味着即使某个元素的处理发生错误，
     * 它不会中断整个流程，而是会继续处理下一个元素。
     */

    @Test
    public void processTest22() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );

        personFlux
                .concatMapDelayError(person -> {
                    if (person.getAge() == 25) {
                        return Flux.error(new RuntimeException("Invalid person"));
                    } else {
                        return Flux.just(person.getName());
                    }
                })
                .subscribe(
                        System.out::println,
                        throwable -> System.err.println("Error: " + throwable.getMessage())
                );
    }

    /**
     * concatWith(Publisher<? extends T> other)
     * 将此通量的发射与所提供的发布服务器连接（无交错）。
     */
    @Test
    public void processTest23() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );

        Flux<PersonFlux> personFlux2 = Flux.just(
                new PersonFlux("Tom", 22),
                new PersonFlux("Jane", 24),
                new PersonFlux("Charlie", 20)
        );

        Flux<PersonFlux> concatenatedFlux = personFlux.concatWith(personFlux2);

        concatenatedFlux.subscribe(person -> System.out.println(person.getName()));
    }

    /**
     * concatWithValues(T... values)
     * 将值连接到通量的末尾
     */
    @Test
    public void processTest24() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );


        Flux<PersonFlux> concatenatedFlux = personFlux.concatWithValues(new PersonFlux("Tom", 22),
                new PersonFlux("Jane", 24),
                new PersonFlux("Charlie", 20));

        concatenatedFlux.subscribe(person -> System.out.println(person.getName()));
    }

    /**
     * count()
     * 统计此通量中的值数。
     */
    @Test
    public void processTest25() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );
        Mono<Long> count = personFlux.count();
        count.subscribe(s -> System.out.println(s));
        // 3
    }

    /**
     * distinct()
     */
    @Test
    public void processTest26() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 30),
                new PersonFlux("Charlie", 20)
        );
        Flux<PersonFlux> distinctFlux = personFlux.distinct();

        distinctFlux.subscribe(person -> System.out.println(person));
    }

    /**
     * distinct(Function<? super T,? extends V> keySelector,
     * Supplier<C> distinctStoreSupplier,
     * BiPredicate<C,V> distinctPredicate,
     * Consumer<C> cleanup)
     * 对于每个订阅服务器，通过在任意用户提供的<C>存储上应用BiPredicate
     * 和通过用户提供的Function提取的密钥进行比较，跟踪已看到的Flux中的元素并过滤出重复项。
     * <p>
     * keySelector：一个函数，用于从元素中提取一个用于比较的键。
     * distinctStoreSupplier：一个供应商函数，用于创建用于存储不重复键的状态对象。
     * distinctPredicate：一个二元谓词，用于判断给定键是否已经存在于存储对象中。
     * cleanup：一个消费者函数，用于在 Flux 完成或取消订阅时清理存储对象。
     */
    @Test
    public void processTest27() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Flux<PersonFlux> distinctFlux = personFlux.distinct(
                PersonFlux::key, // 使用姓名 和年龄 作为键
                HashSet::new,    // 使用 HashSet 作为存储对象
                HashSet::add,    // 使用 HashSet 的 add 方法判断键是否重复
                HashSet::clear   // 在完成或取消订阅时清空 HashSet
        );

        distinctFlux.subscribe(person -> System.out.println(person));
    }

    /**
     * distinct(Function<? super T,? extends V> keySelector,
     * Supplier<C> distinctCollectionSupplier)
     * 对于每个订阅服务器，跟踪已经看到的Flux中的元素，并过滤掉重复的元素，
     * 通过用户提供的Function提取的密钥和提供的Collection（通常是Set）的add方法进行比较。
     */
    @Test
    public void processTest28() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Flux<PersonFlux> distinctFlux = personFlux.distinct(
                PersonFlux::key, // 使用姓名 和年龄 作为键
                HashSet::new    // 使用 HashSet 作为存储对象
        );

        distinctFlux.subscribe(person -> System.out.println(person));
    }

    /**
     * elapsed(Scheduler scheduler)
     * 将此通量映射到时间毫秒和源数据的Tuple2<Long，T>中。
     */
    @Test
    public void processTest29() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                Flux.just(new PersonFlux("Alice", 25)).delayElements(Duration.ofMillis(20)).blockFirst(),
                Flux.just(new PersonFlux("Alice", 25)).delayElements(Duration.ofMillis(40)).blockFirst(),
                Flux.just(new PersonFlux("Bob", 30)).delayElements(Duration.ofMillis(40)).blockFirst(),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Flux<Tuple2<Long, PersonFlux>> elapsedFlux = personFlux
                .elapsed(Schedulers.single());

        elapsedFlux.subscribe(tuple -> {
            Long elapsedMillis = tuple.getT1();
            PersonFlux person = tuple.getT2();
            System.out.println("Person: " + person + ", Elapsed Time (ms): " + elapsedMillis);
        });

        // Wait for the elapsedFlux to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * elementAt(int index, T defaultValue)
     * 仅在给定索引位置发射元素，或者如果序列较短，则回退到默认值。
     * <p>
     * 用于获取流中指定索引处的元素，如果索引超出范围，则返回默认值。
     * index：指定的索引位置，从0开始计数。
     * defaultValue：索引超出范围时返回的默认值。
     */
    @Test
    public void processTest30() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Mono<PersonFlux> personFluxMono = personFlux.elementAt(2, new PersonFlux("Charlie-default", 24));
        personFluxMono.subscribe(person -> System.out.println(person));

        Mono<PersonFlux> personFluxMono2 = personFlux.elementAt(20, new PersonFlux("Charlie-default", 24));
        personFluxMono2.subscribe(person -> System.out.println(person));

        /**
         * PersonFlux(name=Bob, age=30)
         * PersonFlux(name=Charlie-default, age=24)
         */
    }


    /**
     * filter(Predicate<? super T> p)
     * 根据给定的谓词评估每个源值。
     */
    @Test
    public void processTest31() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Flux<PersonFlux> filter = personFlux.filter(p -> p.getAge() > 20 && p.getName().contains("o"));
        filter.subscribe(System.out::println);
        /**
         * PersonFlux(name=Bob, age=30)
         * PersonFlux(name=Bob, age=32)
         */
    }

    /**
     * filterWhen(Function<? super T,? extends Publisher<Boolean>> asyncPredicate, int bufferSize)
     * 使用生成的Publisher＜Boolean＞测试异步测试此Flux发出的每个值
     * <p>
     * 该方法的参数是一个异步谓词（asyncPredicate）函数和缓冲区大小（bufferSize）。
     * 异步谓词函数用来判断流中的元素是否符合过滤条件，返回值是一个Boolean类型的Publisher。
     * 如果异步谓词函数返回的Publisher为true，则保留该元素；
     * 如果返回的为false，则过滤该元素。bufferSize参数用来指定缓冲区的大小。
     */
    @Test
    public void processTest32() throws InterruptedException {
        List<PersonFlux> personList = new ArrayList<>();
        personList.add(new PersonFlux("Alice", 17));
        personList.add(new PersonFlux("Bob", 20));
        personList.add(new PersonFlux("", 25));
        personList.add(new PersonFlux("Tom", 15));
        personList.add(new PersonFlux("Lucy", 19));

        Flux.fromIterable(personList)
                .filterWhen(person -> Mono.justOrEmpty(person.getName())
                        .map(name -> !name.isEmpty())
                        .onErrorReturn(false), 10)
                .filterWhen(person -> Mono.just(person.getAge() >= 18), 10)
                .subscribe(System.out::println);
        /**
         * PersonFlux(name=Bob, age=20)
         * PersonFlux(name=Lucy, age=19)
         */
    }


    /**
     * filter 方法适用于简单的过滤操作，比如对数字、字符串等进行过滤；
     * 而 filterWhen 方法适用于需要异步请求或者需要与其它 Publisher 进行组合计算的过滤操作。
     *
     * 例如，假设要对一个包含下载链接的列表进行过滤，只保留能够成功下载的文件，
     * 可以在异步请求中发送 HTTP 请求，并根据响应状态码等信息判断文件是否下载成功。
     *
     * 在使用 filterWhen 方法时，需要注意以下几点：
     *
     * 异步谓词函数应返回 Mono<Boolean> 或 Flux<Boolean>，表示异步请求的结果；
     *
     * 为了防止堆积过多的请求，通常需要指定缓冲区大小（第二个参数）；
     *
     * 如果异步请求出现异常，需要使用 onErrorReturn
     * 或者 onErrorResume 方法来返回一个默认值，避免程序崩溃。
     *
     */


    /**
     * firstWithSignal(Publisher<? extends I>... sources)
     * 选择第一个发出任何信号的发布服务器（onNext/onError/onComplete），
     * 并重播该发布服务器的所有信号，有效地表现得像这些竞争源中最快的。
     * <p>
     * 可以从多个 Publisher 中获取第一个信号（信号包括onNext、onError和onComplete），
     * 并将该信号作为新的 Flux 进行订阅。如果其中一个 Publisher 产生了信号，
     * 那么其他的 Publisher 就会自动取消订阅。
     * <p>
     * 在使用 firstWithSignal 方法时，需要注意以下几点：
     * <p>
     * 如果多个 Publisher 中同时产生信号，只会获取第一个信号，
     * 其他信号将被忽略，这可能会导致一些未完成的操作未被及时清理，从而引发问题。
     * <p>
     * 由于 firstWithSignal 方法要求所有的 Publisher 具有相同的类型参数，
     * 因此我们需要在创建 Flux 时使用 map 或 flatMap 等操作符将不同类型的数据转换为相同的类型。
     */
    @Test
    public void processTest33() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30)

        );
        Flux<PersonFlux> personFlux2 = Flux.just(
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );

        Mono<PersonFlux> charlie = Mono.just(new PersonFlux("Charlie", 20));

        Flux.firstWithSignal(charlie, personFlux, personFlux2).subscribe(System.out::println);
    }

    /**
     * firstWithValue(Publisher<? extends I> first, Publisher<? extends I>... others)
     * 选择第一个发出任何值的发布服务器，并重播该发布服务器中的所有值，有效地表现为第一个发出onNext的源
     */
    @Test
    public void processTest34() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30)

        );
        Flux<PersonFlux> personFlux2 = Flux.just(
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
    }

    /**
     * groupBy(Function<? super T,? extends K> keyMapper)
     * 根据提供的keyMapper Function生成的结果，将该序列划分为每个唯一键的动态创建的通量（或组）。
     *
     * @throws InterruptedException
     */
    @Test
    public void processTest35() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)

        );

        Mono<List<PersonFlux>> listMono = personFlux.groupBy(person -> Tuples.of(person.getName(), person.getAge()))
                .flatMap(current -> {
                    Tuple2<String, Integer> key = current.key();
                    String t1 = key.getT1();
                    Integer t2 = key.getT2();
                    System.out.println(t1 + ":" + t2);
                    PersonFlux personFluxInit = new PersonFlux(t1 + "_" + t2, 0);
                    Mono<PersonFlux> reduce = current.reduce(personFluxInit, (sum, element) -> {
                        sum.setAge(sum.getAge() + element.getAge());
                        return sum;
                    });
                    return reduce;
                }).collectList();
        listMono.subscribe(System.out::println);

    }

    /**
     * groupBy(Function<? super T,? extends K> keyMapper, Function<? super T,? extends V> valueMapper)
     * keyMapper：将 Flux 中的每个元素 (T) 映射到用于分组的键 (K) 的函数。
     * valueMapper：将 Flux 中的每个元素 (T) 映射到每个组的值 (V) 的函数
     *
     * @throws InterruptedException
     */
    @Test
    public void processTest36() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)

        );

        Flux<GroupedFlux<Tuple2<String, Integer>, String>> groupedFluxFlux = personFlux.groupBy(person -> Tuples.of(person.getName(), person.getAge()), value -> value.getName() + "_" + value.getAge() + "aa");

        groupedFluxFlux.subscribe(e -> {
            Tuple2<String, Integer> key = e.key();
            System.out.println("key:" + key.toString());
            e.subscribe(v -> {
                System.out.println("value:" + v);
            });
        });
        /**
         * key:[Alice,25]
         * value:Alice_25aa
         * value:Alice_25aa
         * key:[Bob,30]
         * value:Bob_30aa
         * key:[Alice,24]
         * value:Alice_24aa
         * key:[Charlie,20]
         * value:Charlie_20aa
         * key:[Bob,32]
         * value:Bob_32aa
         * value:Bob_32aa
         * key:[Charlie1,24]
         * value:Charlie1_24aa
         */

    }

    /**
     * flatMap(Function<? super T,? extends Publisher<? extends V>> mapper, int concurrency, int prefetch)
     * 将此Flux发出的元素异步转换为发布器，然后通过合并将这些内部发布器扁平化为单个Flux，从而允许它们交错。
     * mapper：将 Flux 中的每个元素 (T) 映射成一个 Publisher（V） 的函数。
     * concurrency：并发处理的最大数量。表示同时有多少个元素可以被订阅和处理。
     * prefetch：每个订阅的 Publisher 预取的元素数量。当并发处理时，它可以提高性能。
     *
     * @throws InterruptedException
     */
    @Test
    public void processTest37() throws InterruptedException {
        Flux<PersonFlux> personFlux = Flux.just(
                new PersonFlux("Alice", 25),
                new PersonFlux("Alice", 25),
                new PersonFlux("Bob", 30),
                new PersonFlux("Alice", 24),
                new PersonFlux("Charlie", 20),
                new PersonFlux("Bob", 32),
                new PersonFlux("Bob", 32),
                new PersonFlux("Charlie1", 24)
        );
        Flux<String> stringFlux = personFlux.flatMap(s -> Flux.just(s.getName() + "###" + s.getAge() + "flatMap"), 2, 2);
        stringFlux.subscribe(System.out::println);
    }

    /**
     * flatMapIterable(Function<? super T,? extends Iterable<? extends R>> mapper)
     * 将该通量发射的项目转换为Iterable，然后通过将这些元素合并为单个通量来展平这些元素。
     * mapper：将 Flux 中的每个元素 (T) 映射成一个 Iterable（R） 的函数
     */
    @Test
    public void processTest38() {
        // 创建一个 Book 对象列表
        List<Book> books = Arrays.asList(
                new Book("Book 1", Arrays.asList("Fiction", "Thriller")),
                new Book("Book 2", Arrays.asList("Non-fiction", "History")),
                new Book("Book 3", Arrays.asList("Fiction", "Mystery"))
        );

        // 将列表转换为 Flux
        Flux<Book> bookFlux = Flux.fromIterable(books);

        // 使用 flatMapIterable 进行标签列表的合并
        Flux<String> tagsFlux = bookFlux.flatMapIterable(Book::getTags);

        // 订阅并打印合并后的标签列表
        tagsFlux.subscribe(tag -> System.out.println("Tag: " + tag));
    }

    /**
     * flatMapSequential(Function<? super T,? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch)
     * 将此Flux发出的元素异步转换为发布者，然后将这些内部发布者扁平化为单个Flux，但按其源元素的顺序合并它们。
     * flatMapSequential 与 flatMap 不同之处在于，它会保持原始元素的顺序，而不是无序地合并结果。
     * mapper：将 Flux 中的每个元素 (T) 映射成一个 Publisher（R） 的函数。
     * maxConcurrency：并发处理的最大数量。表示同时有多少个元素可以被订阅和处理。
     * prefetch：每个订阅的 Publisher 预取的元素数量。当并发处理时，它可以提高性能。
     */
    @Test
    public void processTest39() {
        // 创建一个 Book 对象列表
        List<Book> books = Arrays.asList(
                new Book("Book 1", Arrays.asList("Fiction", "Thriller")),
                new Book("Book 2", Arrays.asList("Non-fiction", "History")),
                new Book("Book 3", Arrays.asList("Fiction", "Mystery"))
        );

        // 将列表转换为 Flux
        Flux<Book> bookFlux = Flux.fromIterable(books);

        // 使用 flatMapIterable 进行标签列表的合并
        Flux<String> tagsFlux = bookFlux.flatMapSequential(s -> Flux.fromIterable(s.getTags()), 2, 2);

        // 订阅并打印合并后的标签列表
        tagsFlux.subscribe(tag -> System.out.println("Tag: " + tag));
    }

    /**
     * groupJoin(
     * Publisher<? extends TRight> other,
     * Function<? super T,? extends Publisher<TLeftEnd>> leftEnd,
     * Function<? super TRight,? extends Publisher<TRightEnd>> rightEnd,
     * BiFunction<? super T,? super Flux<TRight>,? extends R> resultSelector)
     * 将两个发布者的值映射到时间窗口中，并在窗口重叠的情况下发出值的组合。
     * groupJoin 操作符用于将一个 Flux 与另一个 Publisher 进行连接，
     * 并且将两者之间的元素进行分组。它将源 Flux 的元素与另一个 Publisher (other) 的元素进行连接，
     * 并根据提供的选择器函数进行分组。然后，使用提供的结果选择器函数将每个分组中的元素组合成最终的结果。
     * <p>
     * other: 另一个 Publisher，用于与源 Flux 进行连接。
     * leftEnd: 将源 Flux 中的每个元素 (T) 映射成一个 Publisher (TLeftEnd) 的函数，该 Publisher 用于决定如何关闭源 Flux 中的组。
     * rightEnd: 将 other Publisher 中的每个元素 (TRight) 映射成一个 Publisher (TRightEnd) 的函数，该 Publisher 用于决定如何关闭 other Publisher 中的组。
     * resultSelector: 用于将源 Flux 中的每个元素与对应的 other Publisher 中的元素进行组合，并返回最终结果的函数。
     */
    @Test
    public void processTest40() {
        // 创建订单 Flux
        Flux<Order> orderFlux = Flux.just(
                new Order(1),
                new Order(2),
                new Order(3)
        );

        // 创建订单项 Flux
        Flux<OrderItem> orderItemFlux = Flux.just(
                new OrderItem(101, 1),
                new OrderItem(102, 1),
                new OrderItem(103, 2),
                new OrderItem(104, 2),
                new OrderItem(105, 3)
        );

        // 使用 groupJoin 进行连接和分组
        /*Flux<String> resultFlux = orderFlux.groupJoin(
                orderItemFlux,
                order -> Flux.just("Order: " + order.getOrderId()),
                orderItem -> Flux.just("OrderItem: " + orderItem.getItemId()),
                (order, orderItems) -> order + " with " + orderItems.collectList().flatMap(list -> Mono.just(list.toString()))
        );*/

        Flux<String> resultFlux = orderFlux.groupJoin(
                orderItemFlux,
                order -> Flux.just("Order: " + order.getOrderId()),
                orderItem -> Flux.just("OrderItem: " + orderItem.getItemId()),
                (order, orderItems) -> order + " with " + orderItems.collectList().flatMap(list -> Mono.just(list.toString()))
        );

        // 订阅并打印最终结果
        resultFlux.subscribe(result -> System.out.println("Result: " + result));
    }

    @Test
    public void test11() {
        Flux<Integer> left = Flux.just(1, 2, 3);
        Flux<String> right = Flux.just("A", "B", "C");

        left.groupJoin(right,
                l -> Flux.just(l + 10), // 左边序列的结束信号
                r -> Flux.just(r + "X"), // 右边序列的结束信号
                (l, rs) -> rs.collectList().map(list -> l + ": " + list)) // resultselector函数，将左侧元素和右侧元素列表合并为字符串
                .subscribe(System.out::println); // 订阅结果流并打印输出
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
