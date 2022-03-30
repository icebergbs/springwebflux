package reactor3.springwebflux.controller.reactorstuday;

import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;

public class HelloWorldHandlerFunction implements HandlerFunction<ServerResponse> {

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        Mono<String> string = request.bodyToMono(String.class);
        Flux<Product>  productFlux = request.bodyToFlux(Product.class);

        //BodyExtractor 是一种请求消息体的提取策略
        Mono<String> stringMono = request.body(BodyExtractors.toMono(String.class));
        Flux<Product> productFlux1 = request.body(BodyExtractors.toFlux(Product.class));

        return ServerResponse.ok().body(BodyInserters.fromObject("Hellow World"));
    }
}
