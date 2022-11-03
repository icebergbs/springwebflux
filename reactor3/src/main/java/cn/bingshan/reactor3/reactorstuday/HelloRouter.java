package cn.bingshan.reactor3.reactorstuday;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class HelloRouter {

    RouterFunction<ServerResponse> routeHello(HellowHandler hellowHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/")
        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                hellowHandler::hello);
    }
}
