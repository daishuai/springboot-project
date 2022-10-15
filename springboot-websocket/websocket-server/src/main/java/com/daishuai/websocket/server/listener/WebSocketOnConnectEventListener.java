package com.daishuai.websocket.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.validation.ValidationException;

/**
 * @author Daishuai
 * @description WebSocket Connect Listener
 * @date 2019/8/10 11:08
 */
@Slf4j
//@Component
public class WebSocketOnConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        
        log.info("WebSocketOnConnectEventListener 参数 {} ...", sha);
        
        String onDisconnectTopic = sha.getFirstNativeHeader("onDisconnectTopic");
        String clientId = sha.getFirstNativeHeader("clientId");
        
        if (StringUtils.isBlank(clientId)) {
            throw new ValidationException("clientId 必填");
        }
        
        if (StringUtils.isBlank(onDisconnectTopic)) {
            //throw new ValidationException("onDisconnectTopic 必填");
        }
        
        log.info("onDisconnectTopic {} ", onDisconnectTopic);
        log.info("clientId {} ", clientId);
        
        sha.getSessionAttributes().put("onDisconnectTopic", onDisconnectTopic);
        sha.getSessionAttributes().put("clientId", clientId);
    }
}
