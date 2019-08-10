package com.daishuai.websocket.client.handler;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

/**
 * @author Daishuai
 * @description 基础的StompSessionHandler
 * @date 2019/8/10 13:47
 */
public abstract class AbstractStompSessionHandler implements StompSessionHandler {
    
    /**
     * 订阅地址
     * @return
     */
    public abstract String getDestination();
    
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    }
    
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }
    
}
