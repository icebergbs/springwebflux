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

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

/**
 * WebClient
 * RestTemplate的响应式版本 WebClient工具类
 */
public class WebClient1 {
    public static final Logger LOG = LoggerFactory.getLogger(WebClient1.class);

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

        //2.2 retireve()  获取响应主体并对其进行解码的最简单方法
        Mono<Person> result2 = webClient.get()
                .uri("/v1/account/{id}", 11)
                .accept(MediaType.APPLICATION_JSON)   //Json作为序列化  MediaType.TEXT_EVENT_STREAM 实现基于流的处理
                .retrieve()
                .bodyToMono(Person.class);

        //2.3 exchange()   如果希望拥有更多的控制权, retrieve()方法就显得无能为例, 可以使用exchange()方法访问整个响应结果
        //该响应结果是一个ClientResponse对象, 通过他可以获取响应码, Cookie等
        Mono<Account> result3 = webClient.get()
                .uri("/account/{id}")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Account.class));

        //2.4 构建RequestBody
        // 如果有一个 Mono  Flux类型的请求体, 可以使用 body()
        Mono<Account> accountMono = Mono.empty();
        Mono<Void> result4 = webClient.post()
                .uri("/account/{id}", 111)
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountMono, Account.class)
                .retrieve()
                .bodyToMono(Void.class);
        // 如果请求对象是一个实际值, 而不是Publish(Flux/ Mono).
        //则可以使用syncBody()作为一种快捷方式来传递请求
        Account account = new Account();
        Mono<Void> result44 = webClient.post()
                .uri("/account/{id}", 11)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(account)
                .retrieve()
                .bodyToMono(Void.class);

        //2.5 From And  Multipart Data
        //当请求体是一个MultiValueMap对象时, WebClient默认发起的是Form表单提交
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "tianyalan");
        map.add("password", "password");
        Mono<String> mono = webClient.post()
                .uri("/login")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
        //如果想提交Multipart Data, 可以使用MultipartBodyBuilder工具类来简化请求的构建过程
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("fieldPar", "fieldvalue");
        builder.part("filePart", new FileSystemResource("log.png"));
        builder.part("jsonPart", new Account("tianyalan"));
        //MultiValueMap构建完成, syncBody()实现请求提交
        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

        //2.6 客户端过滤器
        //可以使用过滤器函数以任何方式拦截和修改请求
        //例如可以使用过滤器功能添加基本认证
        //过滤器将拦截每个WebClient请求并自动添加该消息头
        WebClient clientFilter = WebClient.builder()
                .filter(basicAuthentication("user", "password"))
                .build();

        WebClient clientFilterDef = WebClient.builder()
                .filter(logRequest())
                .build();

        //2.7  处理WebClient错误
        //当状态码为4xx或5xx,  WebClient抛出WebClientResponseException异常,
        //可以用onStatus()方法自定义对异常的处理
        listAccounts();
        //需要注意的是, WebClientResponseException异常只适用于使用retrieve()方法进行远程请求场景,
        //exchange()方法在获取4xx 5xx响应的情况下不会引发异常
        //因此使用exchcange时,须自行检查状态码并以合适的方式处理

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
