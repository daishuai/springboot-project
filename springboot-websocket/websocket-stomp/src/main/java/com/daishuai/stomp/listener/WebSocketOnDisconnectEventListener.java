package com.daishuai.stomp.listener;

import com.daishuai.stomp.common.CommonCache;
import com.daishuai.stomp.service.InstantSocketTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InstantSocketTaskService instantSocketTaskService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String clientId = sha.getUser().getName();
        log.info("On Disconnect,  username is {}", clientId);

        // 客户端连接断开，移除相关的所有任务
        log.info("客户端连接断开，移除相关的所有任务: {}", clientId);
        instantSocketTaskService.removeAllFromScheduledTask(clientId);
        //移除针对客户端所记录的时间戳
        CommonCache.TIMESTAMP_MAP.forEach((k, v) -> {
            if (k != null && k.endsWith(clientId)) {
                CommonCache.TIMESTAMP_MAP.remove(k);
            }
        });
    }
}
