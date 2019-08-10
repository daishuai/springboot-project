package com.daishuai.websocket.client.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 13:45
 */
@Slf4j
@Component
public class DemoSessionHandler extends AbstractStompSessionHandler {
    
    
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("DemoSessionHandler afterConnected");
    }
    
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("DemoSessionHandler handleException");
    }
    
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("DemoSessionHandler handleTransportError");
    }
    
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("payload:{}", JSON.toJSONString(payload));
        log.info("DemoSessionHandler handleFrame");
    }
    
    @Override
    public String getDestination() {
        return "/personalData/pong,/personalData/pong1,/personalData/pong2";
    }
}
