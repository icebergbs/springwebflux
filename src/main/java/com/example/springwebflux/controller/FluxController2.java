package com.example.springwebflux.controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.lang.reflect.Modifier;

public class FluxController2 {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        Flux.range(1, 3).log().subscribe(System.out::println);

        Mono.just(0).map(x -> 1 / x)
                .checkpoint("zero").subscribe(System.out::println);
    }
}
