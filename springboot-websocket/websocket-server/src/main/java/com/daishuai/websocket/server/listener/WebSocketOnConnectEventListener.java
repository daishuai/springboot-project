package com.daishuai.websocket.server.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

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
        //String clientId = sha.getFirstNativeHeader("clientId");
        //sha.getSessionAttributes().put("clientId", clientId);
        log.info("建立连接 :{}", JSON.toJSONString(sessionConnectEvent));
        log.info("建立连接 :{}", JSON.toJSONString(sha));
    }
}
