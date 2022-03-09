package com.example.springwebflux.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * WebClient
 * RestTemplate的响应式版本 WebClient工具类
 */
public class WebClient1 {
    public static void main(String[] args) {

        //1. 创建WebClient, 有两种方式: create()工厂, WebClient Builder
        WebClient webClient = WebClient.create();

        // 目的时针对某一个特定服务进行操作baseUrl
        WebClient webClientUrl = WebClient.create("https://localhost:8081/v1/accounts");

        WebClient webClientBuilder = WebClient.builder().build();

        //1.2. 配置WebClient , WebClient提供了一些自定义的配置
        WebClient webClientDef = WebClient.builder()
                .baseUrl("https://localhost:8081/v1/accounts")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Reactive WebClient")
                .build();

        //2.1 构造Url
        //路径变量
        webClient.get().uri("http://localhost:8081/account/{p1}/{p2}", "var1", "var2");

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("p1", "var1");
        uriVariables.put("p2", 1);
        webClient.get().uri("http://localhost:8081/account/{p1}/{p2}", uriVariables);

        //还可以通过使用uriBuilder来获取对请求信息的完全控制
        listOrders("aa", "token");

    }

    //1. 创建WebClient, 有两种方式: create()工厂, WebClient Builder
    private static WebClient webClient = WebClient.create();

    public static Flux<Sort.Order> listOrders(String username, String token) {
        //一旦我们准备好请求信息, 就可以使用WebClient提供的一些列工具方法完成远程服务的调用
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user/orders")
                        .queryParam("sort", "updated")
                        .queryParam("direction", "desc")
                        .build())
                .header("Authorization", "Basic "
                        + Base64Utils.encodeToString((username + ":" + token).getBytes(StandardCharsets.UTF_8)))
                .retrieve()
                .bodyToFlux(Sort.Order.class);
    }
}
