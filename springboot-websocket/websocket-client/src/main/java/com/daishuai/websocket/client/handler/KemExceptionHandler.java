package com.daishuai.websocket.client.handler;

import com.daishuai.websocket.client.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 13:50
 */
@Slf4j
@ControllerAdvice
public class KemExceptionHandler {
    
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Object handleServiceException(ServiceException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "500");
        result.put("message", e.getMessage());
        result.put("timestamp", System.currentTimeMillis());
        log.error("出现异常：{}", e.getMessage(), e);
        return result;
    }
}
