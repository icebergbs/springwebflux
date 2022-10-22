package cn.bingshan.reactor3.demo.reactorstuday;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Account;
import reactor3.springwebflux.entity.MyCustomClientException;
import reactor3.springwebflux.entity.Person;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * WebClient
 * RestTemplate的响应式版本 WebClient工具类
 */
public class WebClient2 {
    public static final Logger LOG = LoggerFactory.getLogger(WebClient2.class);

    public static void main(String[] args) {

//        WebClient webClient = WebClient.create();
//        // 目的时针对某一个特定服务进行操作baseUrl
//
//
//
//        //2.2 retireve()  获取响应主体并对其进行解码的最简单方法
//        Mono<Object> result2 = webClient.get()
//                .uri("/accounts/{id}", 1)
//                .accept(MediaType.APPLICATION_JSON)   //Json作为序列化  MediaType.TEXT_EVENT_STREAM 实现基于流的处理
//                .retrieve()
//                .bodyToMono(Object.class);
//        System.out.println("result2");
//        result2.flatMap(obj ->  Mono.just(obj))
//                .subscribe(System.out::println);
//       // return result2.flatMap(obj -> ok().contentType(APPLICATION_JSON).body(fromValue(obj)));

        WebClient webClient = WebClient.create("http://localhost:18081");
        Mono<Account> acc = webClient
                .get()
                .uri("/accounts/1")
                .retrieve()
                .bodyToMono(Account.class);
        System.out.println("result2");
        acc.flatMap(obj ->  {
            String aa = obj.toString();
            System.out.println(aa);
            return null;
        });



    }

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

    //自定义过滤器函数: 作用对每个请求做详细的日志记录
    private static ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            LOG.info("Request: {} {}", clientRequest.method(),
                    clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value ->
                            LOG.info("{} = {}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    //
    private static Flux<Account> listAccounts() {
        return webClient.get()
                .uri("/account?sort={sortField}&direction={sortDirection}", "updated", "desc")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new MyCustomClientException()))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new MyCustomClientException()))
                .bodyToFlux(Account.class);
    }

    }
