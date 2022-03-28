package com.example.springwebflux.controller;

import java.util.List;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/26 9:43
 */
public interface MyMessageListener<T> {
    void onMessage(List<T> messages);
}
