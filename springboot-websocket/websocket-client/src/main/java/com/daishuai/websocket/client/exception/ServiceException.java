package com.daishuai.websocket.client.exception;

/**
 * @author Daishuai
 * @description 业务异常
 * @date 2019/8/10 13:51
 */
public class ServiceException extends RuntimeException {
    
    public ServiceException(String message) {
        super(message);
    }
}
