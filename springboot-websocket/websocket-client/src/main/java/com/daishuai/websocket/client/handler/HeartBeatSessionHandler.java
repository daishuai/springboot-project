package com.daishuai.websocket.client.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 15:47
 */
@Slf4j
@Component
public class HeartBeatSessionHandler extends AbstractStompSessionHandler {
    @Override
    public String getDestination() {
        return "/heartBeat/pong";
    }
    
    
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("payload:{}", JSON.toJSONString(payload));
    }
    
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    
    }
    
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
    
    }
}
