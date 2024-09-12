package com.daishuai.websocket.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.daishuai.websocket.server.common.CommonCache;
import com.daishuai.websocket.server.entity.WebSocketClientEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/16 22:55
 */
@Slf4j
@Component
public class KemWebSocketUserInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String clientId = accessor.getFirstNativeHeader("clientId");
            String sessionId = accessor.getSessionId();
            if (StringUtils.isBlank(clientId)) {
                clientId = sessionId;
            }
            WebSocketClientEntity webSocketClientEntity = new WebSocketClientEntity(clientId);
            webSocketClientEntity.setUserId(accessor.getFirstNativeHeader("userId"));
            webSocketClientEntity.setUniqueKey(accessor.getFirstNativeHeader("uniqueKey"));
            webSocketClientEntity.setUserName(accessor.getFirstNativeHeader("userName"));
            accessor.setUser(webSocketClientEntity);

            // 设置当前访问器的认证用户
            log.info("当前访问器的认证用户: {}", JSON.toJSONString(webSocketClientEntity));
            CommonCache.sessionMap.put(clientId, sessionId);
            //throw new MessagingException("鉴权失败");
        }
        return message;
    }
}
