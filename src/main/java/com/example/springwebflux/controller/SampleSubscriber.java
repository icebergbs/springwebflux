package com.example.springwebflux.controller;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 21:03
 */
public class SampleSubscriber<T> extends BaseSubscriber<T> {

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        System.out.println(value);
        request(1);
    }
}
