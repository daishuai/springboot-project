package com.daishuai.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

/**
 * @author Daishuai
 * @description WebSocket Connect Listener
 * @date 2019/8/10 11:08
 */
@Slf4j
@Component
public class WebSocketOnConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        Principal user = sha.getUser();
        log.info("On Connect,  username is {}", user.getName());
    }
}
