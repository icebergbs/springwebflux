package com.bingshan.gateway.exception;

import com.bingshan.gateway.config.GlobalErrorAttributes2;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/29 14:32
 */
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler2 extends AbstractErrorWebExceptionHandler {
    public GlobalErrorWebExceptionHandler2(GlobalErrorAttributes2 errorAttributes,
                                           WebProperties.Resources resources,
                                           ApplicationContext applicationContext,
                                           ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());

    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    public Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(errorPropertiesMap));
    }

    /**
     * 返回message
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to Handle request [");
        message.append(request.method())
                .append(" ")
                .append(request.uri())
                .append("]");
        if (Objects.nonNull(ex)) {
            message.append(" ")
                    .append(ex.getMessage());
        }
        return message.toString();
    }

}
