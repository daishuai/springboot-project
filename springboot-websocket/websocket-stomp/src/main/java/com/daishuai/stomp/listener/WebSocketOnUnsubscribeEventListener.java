package com.daishuai.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;

/**
 * @author Daishuai
 * @description WebSocket Subscribe Listener
 * @date 2019/8/10 11:09
 */
@Slf4j
@Component
public class WebSocketOnUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        Principal user = sha.getUser();
        log.info("On Unsubscribe,  username is {}", user.getName());
    }
}
