package com.example.springwebflux.controller;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_1_Core {

    public static void main(String[] args) {
        //最简单的上手 Flux 和 Mono 的方式就是使用相应类提供的多种工厂方法之一。
        //比如，如果要创建一个 String 的序列，你可以直接列举它们，或者将它们放到一个集合里然后用来创建 Flux
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        //subscribe 方法示例
        //	订阅并触发序列。
        //对每一个生成的元素进行消费。
        //对正常元素进行消费，也对错误进行响应。
        //对正常元素和错误均有响应，还定义了序列正常完成后的回调。

        //对正常元素、错误和完成信号均有响应， 同时也定义了对该 subscribe 方法返回的 Subscription 执行的回调。
        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> {System.out.println("Done");},
                s -> ss.request(10));
        ints.subscribe(ss);


        System.out.println("-------Generate------");

        //4.4 可编程式地创建一个序列
        //如何通过定义相对应的事件（onNext、onError`和`onComplete） 创建一个 Flux 或 Mono。
        // 所有这些方法都通过 API 来触发我们叫做 sink（池） 的事件。


        //sink 的类型
        // 1.Generate
        //这是一种 同步地， 逐个地 产生值的方法，
        // 意味着 sink 是一个 SynchronousSink 而且其 next() 方法在每次回调的时候最多只能被调用一次
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x" + state + " = " + 3*state);
                    if (state == 10)
                        sink.complete();
                    return state + 1;
                }
        );
        flux.subscribe(System.out::println);

        System.out.println("-------mutable------");
        //可变类型的状态变量
        Flux<String> flux1 = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x" + i + " = " + 3*i);
                    if (i == 10)
                        sink.complete();
                    return state;
                }
        );
        flux1.subscribe(System.out::println);

        //2   Create
        //作为一个更高级的创建 Flux 的方式， create 方法的生成方式既可以是同步， 也可以是异步的，并且还可以每次发出多个元素。
        //该方法用到了 FluxSink.   与 generate 不同的是，create 不需要状态值，
        // 另一方面，它可以在回调中触发 多个事件（即使是在未来的某个时间）。

        //create 有个好处就是可以将现有的 API 转为响应式，比如监听器的异步方法。
//        Flux<String> bridge = Flux.create(sink -> {
//            myEventProcessor.register(
//                    new MyEventListener<String>() {
//                        @Override
//                        public void onDataChunk(List<String> chunk) {
//                            for (String s : chunk) {
//                                sink.next(s);
//                            }
//                        }
//
//                        @Override
//                        public void processComplete() {
//                            sink.complete();
//                        }
//                    }
//            );
//        });

        //推送/拉取（push/pull）混合模式
//        Flux<String> bridge2 = Flux.create(sink -> {
//            myMessageProcessor.register(
//                    new MyMessageListener<String>() {
//                        @Override
//                        public void onMessage(List<String> messages) {
//                            for (String s : messages) {
//                                sink.next(s);
//                            }
//                        }
//                    }
//            );
//            sink.onRequest(n -> {
//                List<String> message = myMessageProcessor.request(n);
//                for (String s : message) {
//                    sink.next(s);
//                }
//            });
//        });

        //清理（Cleaning up）
        //onDispose 和 onCancel 这两个回调用于在被取消和终止后进行清理工作。
        // onDispose 可用于在 Flux 完成，有错误出现或被取消的时候执行清理。
        // onCancel 只用于针对“取消”信号执行相关操作，会先于 onDispose 执行。
//        Flux<String> bridge4 = Flux.create(sink -> {
//            sink.onRequest(n -> channel.poll(n))
//                    .onCancel(() -> channel.cannel())
//                    .onDispose(() -> channel.close());
//        });


        System.out.println("-------Handle------");
        //4.4.3 Handle
        //将 handle 用于一个 “映射 + 过滤 null " 的场景
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                                    .handle((i, sink) -> {
                                        String letter = alphabet(i);
                                        if (letter != null)
                                            sink.next(letter);
                                    });
        alphabet.subscribe(System.out::println);

        System.out.println("-------Schedulers------");

        //4.5 调度器（Schedulers)
        Flux<Long> flux2 = Flux.interval(Duration.ofMillis(300), Schedulers.newSingle("test"));
        //flux2.subscribe(System.out::println);

        System.out.println("-------Thread mode------");
        //4.6. 线程模型
        //支持任务共享的多线程模型
        Flux.range(1, 10000)
                .publishOn(Schedulers.parallel())
                .subscribe((s) -> System.out.println(Thread.currentThread().getName() + ":" + s));


        //4.7. 处理错误
        //响应式流中的任何错误都是一个终止事件
        //静态缺省值
        Flux.just(10)
                .map(this::doSomethingDangerous)
                .onErrorReturn("RECOVERED");

        Flux.just(10)
                .map(this::doSomethingDangerous)
                .onErrorReturn(e -> e.getMessage().equals("boom10"), "recovered10");

        //对于每一个 key， 异步地调用一个外部服务。
        //如果对外部服务的调用失败，则再去缓存中查找该 key。注意，这里无论 e 是什么，都会执行异常处理方法。
        Flux.just("key1", "key2")
                .flatMap(k -> callExternalService(k))
                .onErrorResume(e -> getFromCache(k));
        //就像 onErrorReturn，onErrorResume 也有可以用于预先过滤错误内容的方法变体，可以基于异常类或 Predicate 进行过滤。
        // 它实际上是用一个 Function 来作为参数，还可以返回一个新的流序列。
        Flux.just("timeout1", "unknown", "key2")
                .flatMap(k -> callExternalService(k))
                .onErrorResume(error -> {
                    if (error instanceof TimeoutException)
                        return getFromCache(k);
                    else if (error instanceof UnknownKeyException)
                        return registerNewEntry(k, "DEFAULT");
                    else
                        return Flux.error(error);
                });

        //记录错误日志
        //这个方法与其他以 doOn 开头的方法一样，只起副作用（"side-effect"）。
        // 它们对序列都是只读， 而不会带来任何改动。
        LongAdder failureStat = new LongAdder();
        Flux<String> flux4 =
                Flux.just("unknown")
                        .flatMap(k -> callExternalService(k))
                        .doOnError(e -> {
                            failureStat.increment();
                            log("uh oh, falling back, service failed for key " + k);
                        })
                        .onErrorResume(e -> getFromCache(k));
        //使用资源和 try-catch 代码块
        AtomicBoolean isDisposed = new AtomicBoolean();
        Disposable disposableInstance = new Disposable() {
            @Override
            public void dispose() {
                isDisposed.set(true);
            }

            @Override
            public String toString() {
                return "DISPOSABLE";
            }
        };

        Flux<String> flux5 = Flux.using(
                () -> disposableInstance,
                disposable -> Flux.just(disposable.toString()),
                Disposable::dispose
        );

        LongAdder statsCancel = new LongAdder();
        Flux<String> flux6 = Flux.just("foo", "bar")
                                .doFinally(type -> {
                                    if (type == SignalType.CANCEL)
                                        statsCancel.increment();
                                })
                                .take(1);





    }

    // Integer序列， 映射为字母 或 null
    public static String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char)letterIndexAscii;
    }






}
