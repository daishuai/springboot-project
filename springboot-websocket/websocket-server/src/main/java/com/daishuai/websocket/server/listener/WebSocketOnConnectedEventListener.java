package com.daishuai.websocket.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * @author Daishuai
 * @description WebSocket Connected Listener
 * @date 2019/8/10 11:08
 */
@Slf4j
@Component
public class WebSocketOnConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    
    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        
        log.info("WebSocketOnConnectedEventListener 参数 {} ...", sha);
    }
}
