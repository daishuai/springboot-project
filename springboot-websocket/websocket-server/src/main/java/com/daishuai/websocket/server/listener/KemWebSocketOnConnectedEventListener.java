package com.daishuai.websocket.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * @author Daishuai
 * @description WebSocket Connected Listener
 * @date 2019/8/10 11:32
 */
@Slf4j
@Component
public class KemWebSocketOnConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    
    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        GenericMessage genericMessage = (GenericMessage) sha.getHeader("simpConnectMessage");
        MessageHeaders headers = genericMessage.getHeaders();
        headers.forEach((key, value) -> log.info("key:{}, value:{}", key ,value));
        log.info("OnConnected 参数 {} ...", sha);
        String message = sha.getMessage();
        log.info("message:{}", message);
    }
}
