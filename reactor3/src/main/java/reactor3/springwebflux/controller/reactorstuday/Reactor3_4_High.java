package reactor3.springwebflux.controller.reactorstuday;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_4_High {

    public static void main(String[] args) throws InterruptedException {

        //8.1. 打包重用操作符
        //8.1.1. 使用 transform 操作符
        //这个函数式能在操作期（assembly time） 将被封装的操作链中的操作符还原并接入到调用 transform 的位置
        System.out.println("------transform-----");
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                      .map(String::toUpperCase);
        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(b -> System.out.println("Subscriber to Transformed MapAndFilter: " + b));

        //8.1.2. 使用 compose 操作符
        //compose 操作符与 transform 类似，也能够将几个操作符封装到一个函数式中。
        // 主要的区别就是，这个函数式作用到原始序列上的话，是 基于每一个订阅者的（on a per-subscriber basis） 。
        // 这意味着它对每一个 subscription 可以生成不同的操作链（通过维护一些状态值）
        System.out.println("------compose-----");
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap1 =
                f -> {
                    if (ai.incrementAndGet() == 1) {
                        return f.filter(color -> !color.equals("orange"))
                                .map(String::toUpperCase);
                    }
                    return f.filter(color -> !color.equals("purple"))
                            .map(String::toUpperCase);
                };
//        Flux<String> composedFlux =
//                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
//                        .doOnNext(System.out::println)
//                        .compose(filterAndMap);


        //8.2. Hot vs Cold
        System.out.println("------Hot vs Cold------");
        Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .filter(s -> s.startsWith("o"))
                .map(String::toUpperCase);

        source.subscribe(d -> System.out.println("Subscriber 1: "+d));
        source.subscribe(d -> System.out.println("Subscriber 2: "+d));

        //两个订阅者都触发了所有的颜色，因为每一个订阅者都会让构造 Flux 的操作符运行一次。
        //第一个订阅者收到了所有的四个颜色，第二个订阅者由于是在前两个颜色发出之后订阅的， 故而收到了之后的两个颜色
        //从这个例子可见， 无论是否有订阅者接入进来，这个 Flux 都会运行
        System.out.println("------Hot------");
        UnicastProcessor<String> hotSource = UnicastProcessor.create();
        Flux<String> hotFlux = hotSource.publish()
                                        .autoConnect()
                                        .map(String::toUpperCase);
        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: " + d));
        hotSource.onNext("blue");
        hotSource.onNext("green");
        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: " + d));
        hotSource.onNext("orange");
        hotSource.onNext("purple");
        hotSource.onComplete();

        //8.3. 使用 ConnectableFlux 对多个订阅者进行广播
        //publish 会尝试满足各个不同订阅者的需求（背压），并综合这些请求反馈给源。
        // 尤其是如果有某个订阅者的需求为 0，publish 会 暂停 它对源的请求。
        //
        //replay 将对第一个订阅后产生的数据进行缓存，最多缓存数量取决于配置（时间/缓存大小）。
        // 它会对后续接入的订阅者重新发送数据。

        //ConnectableFlux 提供了多种对下游订阅的管理。
        //connect 当有足够的订阅接入后，可以对 flux 手动执行一次。它会触发对上游源的订阅。
        //autoConnect(n) 与 connect 类似，不过是在有 n 个订阅的时候自动触发。
        //refCount(n) 不仅能够在订阅者接入的时候自动触发，还会检测订阅者的取消动作。如果订阅者数量不够， 会将源“断开连接”，再有新的订阅者接入的时候才会继续“连上”源。
        //refCount(int, Duration) 增加了一个 "优雅的倒计时"：一旦订阅者数量太低了，它会等待 Duration 的时间，如果没有新的订阅者接入才会与源“断开连接”。

        System.out.println("------ConnectableFlux------");
        Flux<Integer> source1 = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));
        ConnectableFlux<Integer> co = source1.publish();

        co.subscribe(System.out::println, e -> {}, () -> {});
        co.subscribe(System.out::println, e -> {}, () -> {});

        System.out.println("done subscribing");
        Thread.sleep(500);
        System.out.println("will now connect");
        co.connect();

        System.out.println("------ConnectableFlux  autoConnect------");
        Flux<Integer> source2 = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));
        Flux<Integer> autoCo = source2.publish().autoConnect(2);
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("subscribed first");
        Thread.sleep(500);
        System.out.println("subscribing second");
        autoCo.subscribe(System.out::println, e -> {}, () ->{});

        //8.4. 三种分批处理方式
        //Reactor 总体上有三种方案：分组（grouping）、 窗口（windowing）、缓存（buffering）
        // 这三种在概念上类似，因为它们都是将 Flux<T> 进行聚集。


        //8.4.1. 用 Flux<GroupedFlux<T>> 进行分组
        System.out.println("------Flux<GroupedFlux<T>>------");
        StepVerifier.create(
                Flux.just(1,3,5,2,4,6,11,13,12)
                    .groupBy(i -> i % 2 == 0 ? "even" : "odd")
                    .concatMap(g -> g.defaultIfEmpty(-1)
                                     .map(String::valueOf)
                                     .startWith(g.key()))
        )
                .expectNext("odd", "1", "3", "5", "11", "13")
                .expectNext("even", "2", "4", "6", "12")
                .verifyComplete();

        System.out.println("------window------");
        //8.4.2. 使用 Flux<Flux<T>> 进行 window 操作
        StepVerifier.create(
                Flux.range(1, 10)
                        .window(5, 3) //overlapping windows
                        .concatMap(g -> g.defaultIfEmpty(-1)) //将 windows 显示为 -1
        )
                .expectNext(1, 2, 3, 4, 5)
                .expectNext(4, 5, 6, 7, 8)
                .expectNext(7, 8, 9, 10)
                .expectNext(10)
                .verifyComplete();

        System.out.println("------windowWhile------");
//        StepVerifier.create(
//                Flux.just(1, 3, 5, 2, 4, 6, 11, 12, 13)
//                        .windowWhile(i -> i % 2 == 0)
//                        .concatMap(g -> g.defaultIfEmpty(-1))
//        )
//                .expectNext(-1, -1, -1) //分别被奇数 1 3 5 触发
//                .expectNext(2, 4, 6) // 被 11 触发
//                .expectNext(12) // 被 13 触发
//                .expectNext(-1) // 空的 completion window，如果 onComplete 前的元素能够匹配上的话就没有这个了
//                .verifyComplete();

        //8.4.3. 使用 Flux<List<T>> 进行缓存
        StepVerifier.create(
                Flux.range(1, 10)
                        .buffer(5, 3) // 缓存重叠
        )
                .expectNext(Arrays.asList(1, 2, 3, 4, 5))
                .expectNext(Arrays.asList(4, 5, 6, 7, 8))
                .expectNext(Arrays.asList(7, 8, 9, 10))
                .expectNext(Collections.singletonList(10))
                .verifyComplete();






    }

}
