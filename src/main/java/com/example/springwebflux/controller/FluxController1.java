package com.example.springwebflux.controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class FluxController1 {

    /**
     * Flux 和 Mono操作符
     */
    public static void main(String[] args) {
        //    buffer
        Flux.range(1, 50).buffer(10).subscribe(System.out::println);
        System.out.println("------");
        /**
         * bufferTimeout()指定时间间隔进行收集
         * bufferUntil()会一直收集,直到断言条件返回true
         * bufferWhile() 只有当断言条件返回true时才会收集
         */
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("------");
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);

    }
}
