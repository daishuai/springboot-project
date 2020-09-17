package com.daishuai.websocket.server.listener;

import com.alibaba.fastjson.JSON;
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
//@Component
public class WebSocketOnUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        Object clientId = sha.getSessionAttributes().get("clientId");
        log.info("取消订阅 :{}", JSON.toJSONString(sessionUnsubscribeEvent));
        log.info("取消订阅 :{}", JSON.toJSONString(sha));
        log.info("sessionId:{}, destination:{}, subscriptionId:{}, clientId:{}", sha.getSessionId(), sha.getDestination(), sha.getSubscriptionId(), clientId);
    }
}
