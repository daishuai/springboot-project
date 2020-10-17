package com.daishuai.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

/**
 * 监听订阅地址的用户
 *
 * @author daishuai
 */
@Slf4j
@Component
public class WebSocketOnSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        Principal user = sha.getUser();
        log.info("On Subscribe,  username is {}", user.getName());
    }
}
