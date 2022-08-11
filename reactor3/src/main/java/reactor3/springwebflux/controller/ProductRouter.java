package reactor3.springwebflux.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor3.springwebflux.service.ProductHandler;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> routeProduct(ProductHandler productHandler) {
        RouterFunction<ServerResponse> rf =  RouterFunctions
                .route(RequestPredicates.GET("/route").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProducts)
                .andRoute(RequestPredicates.GET("/route/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProductById)
                .andRoute(RequestPredicates.POST("/route").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::createProduct)
                .andRoute(RequestPredicates.DELETE("/route/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::deleteProduct);
        return rf;
    }
}
