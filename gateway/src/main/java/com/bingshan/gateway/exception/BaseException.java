package com.bingshan.gateway.exception;

public interface BaseException {
    Integer SYSTEM_ERROR = 500;
    Integer SUCCESS = 200;
    Integer getCode();
    String getMessage();
}
