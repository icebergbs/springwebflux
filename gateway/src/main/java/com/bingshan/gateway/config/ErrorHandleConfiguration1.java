//package com.bingshan.gateway.config;
//
//import com.bingshan.gateway.exception.GlobalExceptionHandler1;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.web.ResourceProperties;
//import org.springframework.boot.autoconfigure.web.ServerProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.web.reactive.result.view.ViewResolver;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// *
// *
// * @author  bingshan
// * @date 2022/3/29 12:59
// */
//@Configuration
//@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
//public class ErrorHandleConfiguration1 {
//
//    private final ServerProperties serverProperties;
//
//    private final ApplicationContext applicationContext;
//
//    private final ResourceProperties resourceProperties;
//
//    private final List<ViewResolver> viewResolverList;
//
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//    public ErrorHandleConfiguration1(ServerProperties serverProperties,
//                                     ApplicationContext applicationContext,
//                                     ResourceProperties resourceProperties,
//                                     ObjectProvider<List<ViewResolver>> viewResolverList,
//                                     ServerCodecConfigurer serverCodecConfigurer) {
//        this.serverProperties = serverProperties;
//        this.applicationContext = applicationContext;
//        this.resourceProperties = resourceProperties;
//        this.viewResolverList = viewResolverList.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
//        GlobalExceptionHandler1 exceptionHandler = new GlobalExceptionHandler1(errorAttributes,
//                this.resourceProperties,
//                this.serverProperties.getError(),
//                this.applicationContext);
//        exceptionHandler.setViewResolvers(this.viewResolverList);
//        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
//        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
//        return exceptionHandler;
//    }
//}
