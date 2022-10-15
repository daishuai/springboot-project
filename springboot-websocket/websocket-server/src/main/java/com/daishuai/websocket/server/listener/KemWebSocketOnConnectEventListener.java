package com.daishuai.websocket.server.listener;

import com.daishuai.websocket.server.common.CommonCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author Daishuai
 * @description WebSocket Connect Listener
 * @date 2019/8/10 11:08
 */
@Slf4j
@Component
public class KemWebSocketOnConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        String userId = sha.getFirstNativeHeader("userId");
        String clientId = sha.getFirstNativeHeader("clientId");
        CommonCache.connectMachine.put(clientId, System.currentTimeMillis());
        log.info("设备{}上线，登陆用户{}", clientId, userId);
        log.info("OnConnect 参数 {} ...", sha);
        sha.getSessionAttributes().put("userId", userId);
        sha.getSessionAttributes().put("clientId", clientId);
        String sessionId = sha.getSessionId();
        Principal user = sessionConnectEvent.getUser();
        String name = user.getName();
        stringRedisTemplate.opsForValue().set("WebSocketSession:" + name, sessionId);
    }
}
