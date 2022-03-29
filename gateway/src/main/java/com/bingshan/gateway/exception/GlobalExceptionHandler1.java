package com.bingshan.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/29 12:01
 */
@Slf4j
public class GlobalExceptionHandler1 extends DefaultErrorWebExceptionHandler {
    public GlobalExceptionHandler1(ErrorAttributes errorAttributes,
                                   WebProperties.Resources resources,
                                   ErrorProperties errorProperties,
                                   ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        int code ;
        Throwable error = super.getError(request);
        if (error instanceof ChangeSetPersister.NotFoundException) {
            code = HttpStatus.NOT_FOUND.value();
        } else if (error instanceof ResponseStatusException){
            ResponseStatusException responseStatusException = (ResponseStatusException) error;
            code = responseStatusException.getStatus().value();
        } else {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        return response(code, buildMessage(request, error));
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        if (Objects.isNull(errorAttributes.get("code"))) {
            log.warn("获取HttpStatus失败 ： {}", errorAttributes);
            return 500;
        } else {
            int statusCode = (int) errorAttributes.get("code");
            return statusCode;
        }
    }

    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.method());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (Objects.nonNull(ex)) {
            message.append(" ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    public static Map<String, Object> response(int status, String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("errorCode", status);
        resp.put("message", message);
        resp.put("data", null);
        resp.put("sucesss", false);
        return resp;
    }
}
