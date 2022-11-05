package reactor3.springwebflux.controller;

import cn.bingshan.reactor3.reactorstuday.HelloWorldHandlerFunction;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * 3. 构建响应式RESTful服务
 */
public class Reactor_restful {

    /**
     * Spring-webflux 所支持的两种开发模式:
     *    一种是类似于Spring WebMVC的基于注解(@Controller @RequestMapping)
     *    另一种则被称为 Router Functions
     *
     *   Mono.doOnNext() 方法将Mono对象转换为普通的POJO对象并进行保存.
     *
     * Webflow 支持使用与 SpringMVC相同的注解,两者的主要区别在于底层核心通信方式是否阻塞.
     * 响应式Controller操作的是非阻塞的ServerHttpRequest 和 ServerHttpResponse对象.
     *
     */

    /**
     * 3.2.4 使用函数式编程模型创建响应式RESTful服务
     *   1. 函数式编程模型
     *      函数式编程模型的核心概念是Route Functions, 对标@Controller @RequestMapping 等 标准的SpringMVC注解.
     *  Route Function提供一套函数式风格的API, 用于创建Router 和 Handler对象
     *      简单的把 Handler对应为@Controller,  Router 对应成 @RequestMapping
     *      当我们发起一个远程调用时,传入的HTTP请求由HandlerFunction处理. Handler Function 本质上是一个接受ServerRequest
     *  并返回Mono<ServerResponse> 的函数. Server Request 和 ServerResponse是一对不可变接口,用来提供对底层HTTP消息的友好访问.
     *    (1) ServerRequest
     *        ServerRequest可以访问各种HTTP请求元素,包括请求方法, url和参数, 以及 ServerRequest.Headers获取HTTP请求头信息.
     *        bodyToXxx()提供对请求消息体进行访问
     *    (2) ServerResponse
     *        提供对HTTP响应的访问
     *
     *    (3) HandlerFunction
     *        将ServerRequest 和 ServerResponse 组合在一起.
     *        HandlerFunction 是一个接口,可以通过实现该接口中的handler()方法来创建定制化的请求响应处理机制.
     *        例如:  HelloWorldHandlerFunction.java
     *
     *    (4) RouterFunction
     *        HandlerFunction创建了请求的处理逻辑, RouterFunction, 把具体请求与这种处理逻辑关联起来.
     *        RouterFunction将传入请求路由到具体的处理函数,它接受ServerRequest并返回一个Mono<HandlerFunction>
     *
     *           通过请求谓词(Request Predicate) 处理函数进行创建
     *
     *    2. 使用函数式编程模型创建响应式RESTful 服务
     *       HelloRoute.java  HellowHandler.java
     *
     */


    public static void main(String[] args) {
        /**
         * (4) 通过请求谓词(Request Predicate) 处理函数进行创建
         */
        RouterFunction<ServerResponse> hellowWorldRoute = RouterFunctions.route(RequestPredicates.path("/hello-world"),
                new HelloWorldHandlerFunction());

        /**
         * 组合路由. 按照顺序进行评估
         */
        RouterFunction<ServerResponse> personRoute = RouterFunctions.route(GET("/person/{id}").and(accept(MediaType.APPLICATION_JSON)),
                new HelloWorldHandlerFunction())
                .andRoute(GET("/person").and(accept(MediaType.APPLICATION_JSON)),
                        new HelloWorldHandlerFunction())
                .andRoute(POST("/person").and(contentType(MediaType.APPLICATION_JSON)),
                        new HelloWorldHandlerFunction());
    }

    /**
     * 全栈式响应式编程：
     *      响应式开发方式的有效性取决于在整个请求链路的各个环节是否都采用了响应式编程模型。
     *      如果某一个环节或步骤不是响应式的，就会出现同步阻塞，从而导致背压机制无法生效。
     *      如果某一层组件（例如数据访问层）无法采用响应式编程模型，那么响应式编程的概念对于
     *      整个请求链路的其他层而言就没有意义。
     *
     */




}
