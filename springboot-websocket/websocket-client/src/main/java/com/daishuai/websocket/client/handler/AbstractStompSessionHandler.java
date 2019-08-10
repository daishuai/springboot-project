package com.daishuai.websocket.client.handler;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 13:47
 */
public abstract class AbstractStompSessionHandler implements StompSessionHandler {
    
    /**
     * 订阅地址
     * @return
     */
    public abstract String getDestination();
    
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }
}
