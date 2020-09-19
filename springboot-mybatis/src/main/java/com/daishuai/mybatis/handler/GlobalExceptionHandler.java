package com.daishuai.mybatis.handler;

import com.daishuai.mybatis.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    public String serviceExceptionHandler(ServiceException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }
}
