package com.daishuai.websocket.server.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 监听订阅地址的用户
 */
@Slf4j
//@Component
public class WebSocketOnSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());


        String sessionId = sha.getSessionId();
        String subscriptionId = sha.getSubscriptionId();
        String destination = sha.getDestination();
        Object clientId = sha.getSessionAttributes().get("clientId");
        //JSONArray jsonArray = JSON.parseArray(sha.getFirstNativeHeader("params"));
        String params = sha.getFirstNativeHeader("params");
        JSONObject jsonObject = JSON.parseObject(params);
        System.out.println(params);
        log.info("订阅 :{}", JSON.toJSONString(sessionSubscribeEvent));
        log.info("订阅 :{}", JSON.toJSONString(sha));
        log.info("sessionId:{}, destination:{}, subscriptionId:{}, clientId:{}", sessionId, destination, subscriptionId, clientId);

    }
}
