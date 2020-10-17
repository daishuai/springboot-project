package com.daishuai.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * 断开事件,当某个客户端断开连接之后.发送消息到某个topic
 *
 * @author daishuai
 */
@Slf4j
@Component
public class WebSocketOnDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        Principal user = sha.getUser();
        log.info("On Disconnect,  username is {}", user.getName());
    }
}
