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
    }
}
