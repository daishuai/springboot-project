package com.daishuai.websocket.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * @author Daishuai
 * @description WebSocket Subscribe Listener
 * @date 2019/8/10 11:09
 */
@Slf4j
@Component
public class WebSocketOnUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {
    
    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        
        log.info("WebSocketOnUnsubscribeEventListener 参数 {} ...", sha);
    }
}
