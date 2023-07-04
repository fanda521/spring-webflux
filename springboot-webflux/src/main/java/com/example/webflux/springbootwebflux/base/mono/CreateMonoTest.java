package com.example.webflux.springbootwebflux.base.mono;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author jeffrey
 * @version 1.0
 * @date 2023/7/4
 * @time 10:38
 * @week 星期二
 * @description 创建mono的方式
 **/
public class CreateMonoTest {

    //// 简单的实例创建

    /**
     * just(data)
     * 创建一个发出指定项的新Mono，该项在实例化时捕获。
     */
    @Test
    public void createTest01() {
        Mono.just(1).log().subscribe(System.out::println);
    }

    /**
     * justOrEmpty(data)
     * 如果Optional.isPresent（），则创建一个发出指定项的新Mono。否则仅发出onComplete
     */
    @Test
    public void createTest02() {
        String str = null;
        Mono.justOrEmpty(str).log().subscribe(System.out::println);

        String str2 = "";
        Mono.justOrEmpty(str2).log().subscribe(System.out::println);

        Mono.justOrEmpty(2).log().subscribe(System.out::println);
    }

    /**
     * never()
     * 返回一个Mono，它永远不会发出任何数据、错误或完成信号，基本上是无限期运行的。
     */
    @Test
    public void createTest03() {
        Mono.never().log().subscribe(System.out::println);
    }

    /**
     * from(Publisher<? extends T> source)
     * 使用Mono API公开指定的发布服务器，并确保它将发出0或1项。
     */
    @Test
    public void createTest04() {
        // 从Mono 中创建
        Mono.from(Mono.just(2)).log().subscribe(System.out::println);
        // 从flux 中创建，取第一个元素值
        Mono.from(Flux.just(2,4,6)).log().subscribe(System.out::println);
    }

    /**
     * fromRunnable(Runnable runnable)
     * 创建一个Mono，一旦执行了提供的Runnable，它就变成空的。
     */
    @Test
    public void createTest05() {
        Mono.fromRunnable(()->{
            System.out.println("runnable run ...");
        }).log().subscribe(System.out::println);

    }

    /**
     * fromCallable(Callable<? extends T> supplier)
     * 使用提供的Callable创建一个产生其价值的Mono。
     */
    @Test
    public void createTest06() {
        Mono.fromCallable(() ->{
            System.out.println("callable run ...");
            return 23;
        }).log().subscribe(System.out::println);
    }

    /**
     * fromSupplier(Supplier<? extends T> supplier)
     * 创建一个Mono，使用所提供的供应商来产生其价值。
     */
    @Test
    public void createTest07() {
        Mono.fromSupplier(() ->{
            System.out.println("supplier run ...");
            return "supplier";
        }).log().subscribe(System.out::println);
    }

    /**
     * fromDirect(Publisher<? extends I> source)
     * 将Publisher转换为Mono，而不进行任何基数检查（即此方法不会取消第一个元素之后的源）
     */
    @Test
    public void createTest08() {
        Mono.fromDirect(Flux.just(1,2,3)).log().subscribe(System.out::println);
        Mono.fromDirect(Mono.just(23)).log().subscribe(System.out::println);
    }

    /**
     * fromCompletionStage(CompletionStage<? extends T> completionStage)
     * 创建一个Mono，使用提供的CompletionStage产生其价值。
     */
    @Test
    public void createTest09() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            return 22;
        }, threadPool);
        Mono.fromCompletionStage(supplyAsync).log().subscribe(System.out::println);

    }

    /**
     * fromFuture(CompletableFuture<? extends T> future)
     * 创建一个Mono，使用提供的CompletableFuture生成其值，如果Mono被取消，则取消该值
     */
    @Test
    public void createTest10() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            return "fromFuture";
        }, threadPool);
        Mono.fromFuture(supplyAsync).log().subscribe(System.out::println);

    }

    /**
     * fromFuture(CompletableFuture<? extends T> future, boolean suppressCancel)
     * 创建一个Mono，使用提供的CompletableFuture生成其值，如果Mono被取消（如果suppressCancel==false），则可以选择取消future
     */
    @Test
    public void createTest11() {
        // 创建一个CompletableFuture对象
        CompletableFuture<String> future = new CompletableFuture<>();
        // 创建一个Mono，使用CompletableFuture的结果进行完成
        Mono.fromFuture(future, true).log().subscribe(
                // 订阅Mono并定义对结果的处理
                result -> System.out.println("结果: " + result),
                error -> System.out.println("错误: " + error.getMessage()),
                () -> System.out.println("完成")
        );
        // 手动完成CompletableFuture
        future.complete("Hello World!");
        //future.cancel(true);
    }


    /**
     * error(Throwable error)
     * 创建一个在订阅后立即终止并出现指定错误的Mono。
     */
    @Test
    public void createTest12() {
        Mono.error(new RuntimeException("ERROR ...")).log().subscribe(System.out::println);
    }

    /**
     * create(Consumer<MonoSink<T>> callback)
     * 创建一个延迟发射器，该发射器可与基于回调的API一起使用，以发出最多一个值、完整信号或错误信号。
     */
    @Test
    public void createTest13() {
        Mono.create(sink ->{
            System.out.println("create ...");
            sink.success("create");
        }).log().subscribe(System.out::println);
    }

    /**
     * delay(Duration duration)
     * 创建一个Mono，它将onNext信号在默认的Scheduler上延迟给定的持续时间并完成。
     */
    @Test
    public void createTest14() throws InterruptedException {
        Mono.delay(Duration.ofSeconds(1)).map(s -> 22).log().subscribe(System.out::println);
        // 避免主线程先走完，看不到结果
        TimeUnit.MILLISECONDS.sleep(2000);
    }


}
