package com.daishuai.websocket.client.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/8 12:47
 */
@Slf4j
@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("连接成功");
    }
    
    @Override
    public Type getPayloadType(StompHeaders headers) {
        log.info("getPayloadType ");
        return String.class;
    }
    
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("handleFrame,payload:{}", payload);
    }
    
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("出错：{}", exception.getMessage(), exception);
        log.info("handleException ");
    }
    
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("handleTransportError ");
    }
}
