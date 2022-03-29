package com.bingshan.gateway.config;

import com.bingshan.gateway.exception.LinkerRuntimeException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/29 19:45
 */
@Component
public class GlobalErrorAttributes2 extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error =  getError(request);
        //code
        if (error instanceof LinkerRuntimeException) {
            errorAttributes.put("code", ((LinkerRuntimeException)error).getCode());
        } else if (error instanceof ResponseStatusException){
            ResponseStatusException responseStatusException = (ResponseStatusException) error;
            errorAttributes.put("code", responseStatusException.getStatus().value());
        } else {
            errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //message
        errorAttributes.put("message", buildMessage(request, error));
        return errorAttributes;
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
