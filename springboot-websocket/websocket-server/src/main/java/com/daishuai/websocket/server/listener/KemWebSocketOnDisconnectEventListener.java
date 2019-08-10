package com.daishuai.websocket.server.listener;

import com.daishuai.websocket.server.service.KdWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 断开事件,当某个客户端断开连接之后.发送消息到某个topic
 */
@Slf4j
@Component
public class KemWebSocketOnDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    
    @Autowired
    private KdWebSocketService webSocketService;
    
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        
        log.info("OnDisconnect 参数 {} ...", sha);
        
        
    }
}
