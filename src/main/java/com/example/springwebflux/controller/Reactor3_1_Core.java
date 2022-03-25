package com.example.springwebflux.controller;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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
        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<String>() {
                        @Override
                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }

                        @Override
                        public void processComplete() {
                            sink.complete();
                        }
                    }
            );
        });




    }
}
