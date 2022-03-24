package com.example.springwebflux.controller;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

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
    }
}
