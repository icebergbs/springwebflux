package com.bingshan.gateway.filter;

import com.bingshan.gateway.exception.LinkerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
//@Configuration
public class GalRouteFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header("MyHeader", "Myheader Value");
        chain.filter(exchange.mutate().request(builder.build()).build());
        log.info("自定义全局网关过滤器. 针对Request Header的各种处理");
        try {
            int a = 1/0;
        } catch (Exception e) {
            throw new LinkerRuntimeException(200, "GateWayLinker Exception");
        }


        return chain.filter(exchange.mutate().request(builder.build()).build());
    }
}
