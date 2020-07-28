package com.daishuai.websocket.server.listener;

import com.alibaba.fastjson.JSON;
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
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        String sessionId = sha.getSessionId();
        log.info("建立连接 ED:{}", JSON.toJSONString(sessionConnectedEvent));
        log.info("建立连接 ED:{}", JSON.toJSONString(sha));
    }
}
