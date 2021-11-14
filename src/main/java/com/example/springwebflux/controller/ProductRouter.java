package com.example.springwebflux.controller;

import com.example.springwebflux.service.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import javax.print.attribute.standard.Media;
@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> routeProduct(ProductHandler productHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/route").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProducts)
                .andRoute(RequestPredicates.GET("/route/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProductById)
                .andRoute(RequestPredicates.POST("/route").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::createProduct)
                .andRoute(RequestPredicates.DELETE("/route/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::deleteProduct);
    }
}
