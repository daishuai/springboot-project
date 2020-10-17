package com.daishuai.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;

/**
 * @author Daishuai
 * @description WebSocket Connected Listener
 * @date 2019/8/10 11:08
 */
@Slf4j
@Component
public class WebSocketOnConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        Principal user = sha.getUser();
        log.info("On Connected,  username is {}", user.getName());
    }
}
