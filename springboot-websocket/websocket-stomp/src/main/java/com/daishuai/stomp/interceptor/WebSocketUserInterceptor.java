package com.daishuai.stomp.interceptor;

import com.daishuai.stomp.entity.WebSocketClientEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/16 22:55
 */
@Slf4j
@Component
public class WebSocketUserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String clientId = accessor.getFirstNativeHeader("clientId");

            if (StringUtils.isBlank(clientId)) {
                clientId = accessor.getSessionId();
            }
            // 设置当前访问器的认证用户
            log.info("当前访问器的认证用户: {}", clientId);
            accessor.setUser(new WebSocketClientEntity(clientId));
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return null;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

    }
}
