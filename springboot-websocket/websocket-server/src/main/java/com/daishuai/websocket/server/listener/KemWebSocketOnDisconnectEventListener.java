package com.daishuai.websocket.server.listener;

import com.daishuai.websocket.server.service.KdWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 断开事件,当某个客户端断开连接之后.发送消息到某个topic
 */
@Slf4j
@Component
public class KemWebSocketOnDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        String sessionId = sessionDisconnectEvent.getSessionId();
        System.out.println(sessionId);
        CloseStatus closeStatus = sessionDisconnectEvent.getCloseStatus();
        log.info("socket disconnected, status: {}, reason: {}", closeStatus.getCode(), closeStatus.getReason());

        Principal user = sessionDisconnectEvent.getUser();
        if (user == null) {
            return;
        }
        String name = user.getName();
        /*if (StringUtils.equals(sessionId, stringRedisTemplate.opsForValue().get("WebSocketSession:" + name))) {
            System.out.println("同一个Session: " + sessionId);
        } else {
            System.out.println("不是同一个Session: " + sessionId);
        }*/
    }
}
