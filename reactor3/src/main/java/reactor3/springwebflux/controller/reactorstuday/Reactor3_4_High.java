package reactor3.springwebflux.controller.reactorstuday;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_4_High {

    //
    static final String HTTP_CORRELATION_ID = "reactive.http.library.correlationId";

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

        //8.5. 使用 ParallelFlux 进行并行处理
        //如今多核架构已然普及，能够方便的进行并行处理是很重要的。
        // Reactor 提供了一种特殊的类型 ParallelFlux 来实现并行，它能够将操作符调整为并行处理方式。
        System.out.println("------ parallel() 操作符 ------");
        //你可以对任何 Flux 使用 parallel() 操作符来得到一个 ParallelFlux.
        // 不过这个操作符本身并不会进行并行处理，而是将负载划分到多个“轨道（rails）”上 （默认情况下，轨道个数与 CPU 核数相等）。
        Flux.range(1, 10)
                .parallel(2)
                .subscribe(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));
        //为了配置 ParallelFlux 如何并行地执行每一个轨道，你需要使用 runOn(Scheduler)。
        // 注意，Schedulers.parallel() 是推荐的专门用于并行处理的调度器。
        System.out.println("------ runOn(Scheduler) ------");
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
                .subscribe(i -> System.out.println(Thread.currentThread().getName() + " -> " +i));

        //8.6. 替换默认的 Schedulers   xxxx没看懂



        //8.7. 使用全局的 Hooks



        //8.8. 增加一个 Context 到响应式序列
        System.out.println("------ Context ------");
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(s ->
                    Mono.deferContextual(ctx ->
                            Mono.just(s + " " + ctx.get(key))))
                .contextWrite(ctx -> ctx.put(key, "World"));
        r.subscribe(System.out::println);
        StepVerifier.create(r)
                .expectNext("Hello World")
                .verifyComplete();

        //写入 与 读取 Context 的 相对位置 很重要：因为 Context 是不可变的，它的内容只能被上游的操作符看到，
        System.out.println("------ 写入 与 读取 Context 的 相对位置  ------");
        String key1 = "message";
        Mono<String> r1 = Mono.just("Hello")
                .subscriberContext(ctx -> ctx.put(key1, "World"))
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.getOrDefault(key1, "Stranger")));

        StepVerifier.create(r1)
                .expectNext("Hello Stranger")
                .verifyComplete();

        System.out.println("------  Context 的不可变性  ------");
        String key2 = "message";
        Mono<String> r2 = Mono.subscriberContext()
                .map( ctx -> ctx.put(key2, "Hello"))
                .flatMap( ctx -> Mono.subscriberContext())
                .map( ctx -> ctx.getOrDefault(key2,"Default"));

        StepVerifier.create(r2)
                .expectNext("Default")
                .verifyComplete();

        //如果多次对 Context 中的同一个 key 赋值的话，要看 写入的相对顺序 ：
        //读取 Context 的操作符只能拿到下游最近的一次写入的值
        System.out.println("------  写入的相对顺序  ------");
        String key3 = "message";
        Mono<String> r3 = Mono.just("Hello")
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.get(key3)))
                .subscriberContext(ctx -> ctx.put(key3, "Reactor"))
                .subscriberContext(ctx -> ctx.put(key3, "World"));

        StepVerifier.create(r3)
                .expectNext("Hello Reactor")
                .verifyComplete();

        // Context 是与 Subscriber 关联的，而每一个操作符访问的 Context 来自其下游的 Subscriber。
        System.out.println("------ Context 是与 Subscriber 关联的 ------");
        String key4 = "message";
        Mono<String> r4 = Mono.just("Hello")
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.get(key4)))
                .subscriberContext(ctx -> ctx.put(key4, "Reactor"))
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.get(key4)))
                .subscriberContext(ctx -> ctx.put(key4, "World"));

        StepVerifier.create(r4)
                .expectNext("Hello Reactor World")
                .verifyComplete();

        //对 Context 的赋值也可以在一个 flatMap 内部
        //传播（Propagation） + 不可变性（immutability）将类似 flatMap 这样的操作符中的创建的内部序列中的 Context 与外部隔离开来。
        System.out.println("------ flatMap 内部 ------");
        String key5 = "message";
        Mono<String> r5 =
                Mono.just("Hello")
                        .flatMap( s -> Mono.subscriberContext()
                                .map( ctx -> s + " " + ctx.get(key5))
                        )
                        .flatMap( s -> Mono.subscriberContext()
                                .map( ctx -> s + " " + ctx.get(key5))
                                .subscriberContext(ctx -> ctx.put(key5, "Reactor"))
                        )
                        .subscriberContext(ctx -> ctx.put(key5, "World"));

        StepVerifier.create(r5)
                .expectNext("Hello World Reactor")
                .verifyComplete();


        System.out.println("------ 完整的例子 ------");



    }

    @Test
    public void contextForLibraryReactivePut() {
        Mono<String> put = doPut("www.example.com", Mono.just("Walter"))
                .subscriberContext(Context.of(HTTP_CORRELATION_ID, "2-j3r9afaf92j-afkaf"))
                .filter(t -> t.getT1() < 300)
                .map(Tuple2::getT2);

        StepVerifier.create(put)
                .expectNext("PUT <Walter> sent to www.example.com with header X-Correlation-ID = 2-j3r9afaf92j-afkaf")
                .verifyComplete();
    }


    public Mono<Tuple2<Integer, String>> doPut(String url, Mono<String> data) {
        Mono<Tuple2<String, Optional<Object>>> dataAndContext =
                data.zipWith(Mono.subscriberContext()
                        .map(c -> c.getOrEmpty(HTTP_CORRELATION_ID)));

        return dataAndContext
                .<String>handle((dac, sink) -> {
                    if (dac.getT2().isPresent()) {
                        sink.next("PUT <" + dac.getT1() + "> sent to " + url + " with header X-Correlation-ID = " + dac.getT2().get());
                    }
                    else {
                        sink.next("PUT <" + dac.getT1() + "> sent to " + url);
                    }
                    sink.complete();
                })
                .map(msg -> Tuples.of(200, msg));
    }


}
