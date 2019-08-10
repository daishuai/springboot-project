package com.daishuai.websocket.server.listener;

import com.daishuai.websocket.server.service.KdWebSocketService;
import com.daishuai.websocket.server.vo.KdWebSocketMsgDefaultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 断开事件,当某个客户端断开连接之后.发送消息到某个topic
 */
@Slf4j
@Component
public class WebSocketOnDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    
    @Autowired
    private KdWebSocketService webSocketService;
    
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        
        log.info("WebSocketOnDisconnectEventListener 参数 {} ...", sha);
        
        if (sha.getSessionAttributes().get("onDisconnectTopic") != null) {
            String onDisconnectTopic = (String) sha.getSessionAttributes().get("onDisconnectTopic");
            String clientId = (String) sha.getSessionAttributes().get("clientId");
            webSocketService.send(KdWebSocketMsgDefaultVo.builder().payload(clientId + "断开连接").destination(onDisconnectTopic).build());
        }
    }
}
