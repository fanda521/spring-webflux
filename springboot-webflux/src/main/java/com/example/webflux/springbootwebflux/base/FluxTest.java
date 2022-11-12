package com.example.webflux.springbootwebflux.base;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.interfaces.PBEKey;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @Aythor lucksoul 王吉慧
 * @date 2022/10/12 14:16
 * @description
 */
public class FluxTest {
    @Test
    public void fluxHandFirstLast() {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6, 7);
        Mono<Integer> last = just.last();
        Mono<Integer> first = just.next();
        last.subscribe(System.out::println);
        first.subscribe(System.out::println);
    }

    @Test
    /**
     * 阻塞是获取元素
     */
    public void fluxBlockget(){
        Flux<String> flux = Flux.create(skin -> {
            for (int i = 0; i < 2; ++i) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                skin.next("这是第" + i + "个元素");
            }
            skin.complete();
        });
//flux订阅者所有操作都是无副作用的,即不会改变原flux对象数据
//阻塞式订阅,只要有一个元素进入Flux
        String first = flux.blockFirst();
//输出:  这是第0个元素
        System.out.println(first);
//还是输出: 这是第0个元素
        System.out.println(flux.blockFirst());
//输出: 这是第1个元素
        System.out.println(flux.blockLast());
//还是输出: 这是第1个元素
        System.out.println(flux.blockLast());

    }
    @Test
    public void fluxDoON() {
        Flux.just(1,2,3,4,5,6,7,8,9)
                .concatWith(Flux.error(new Exception()))
                //错误时执行
                .doOnError(e -> System.out.println("报错：" + e))
                //完成时执行
                .doOnComplete(()-> System.out.println("数据接收完成"))
                //最后执行
                .doFinally(t-> System.out.println("最后执行信息：" + t))
                .subscribe(System.out::println);

    }
    @Test
    public void fluxDoNo2() {
        //消费者参与前执行的最后一件事，入参为消费者对象（一般用于修改、添加、删除源数据流）
        Flux.just(1,2,3,4,5,6,7,8,9)
                .log()
                .doOnSubscribe(i ->{
                    System.out.println("先请求2个");
                    i.request(2);
                    System.out.println("再请求3个");
                    i.request(3);
                    i.cancel();
                    System.out.println("取消监听");
                })
                .subscribe(System.out::println);

    }

    @Test
    public void fluxDoNoOrig() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10L))
                .subscribe(new Subscriber<Long>() {
                    Subscription subscription;
                    AtomicInteger count = new AtomicInteger();
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        //首先请求5个
                        subscription.request(5);
                        count.set(5);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.print(" value:" + aLong);
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (count.decrementAndGet() <= 0){
                            System.out.println("    消费完成，重新请求5个");
                            subscription.request(5);
                            count.set(5);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("全部消费完成");
                    }
                });
        Thread.sleep(5000L);

    }
    @Test
    public void fluxBasicSus() {
        Flux.range(1,50)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    private int count = 0;
                    private final int limit = 5;
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(limit);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (++count == limit){
                            request(count);
                            count = 0;
                        }
                    }
                });
    }
    @Test
    public void fluxLimitRate() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10L))
                .take(10)
                .log()
                .limitRate(4)
                .subscribe();
        Thread.sleep(1000L);

    }


}
