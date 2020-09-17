package com.daishuai.websocket.server.interceptor;

import com.daishuai.websocket.server.entity.WebsocketClientEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/8/20 17:05
 */
@Slf4j
public class WebsocketClientInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String name = accessor.getFirstNativeHeader("name");
            String sessionId = accessor.getSessionId();
            log.info("当前访问器的认证用户为: {}", name);
            accessor.setUser(new WebsocketClientEntity(sessionId));
        }
        return message;
    }
}
