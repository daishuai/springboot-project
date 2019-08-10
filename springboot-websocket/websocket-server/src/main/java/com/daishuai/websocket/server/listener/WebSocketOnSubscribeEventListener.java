package com.daishuai.websocket.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 监听订阅地址的用户
 */
@Slf4j
@Component
public class WebSocketOnSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        
        log.info("WebSocketOnSubscribeEventListener 参数 {} ...", sha);
    }
}
