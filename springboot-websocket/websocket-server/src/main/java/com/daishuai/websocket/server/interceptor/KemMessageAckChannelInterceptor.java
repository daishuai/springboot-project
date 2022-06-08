package com.daishuai.websocket.server.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;

/**
 * @author Daishuai
 * @date 2021/6/4 15:57
 */
@Slf4j
@Component
public class KemMessageAckChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || StompCommand.ACK != accessor.getCommand()) {
            return;
        }
        String username;
        Principal user = accessor.getUser();
        String messageId = accessor.getFirstNativeHeader("messageId");
        log.info("accessor: {}", JSON.toJSONString(accessor));
        log.debug("收到消息ack回执{}", messageId);

        if (StringUtils.isBlank(messageId)) {
            return;
        }
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "unknownHost";
            log.error("获取服务器IP出错: {}", e.getMessage(), e);
        }
    }
}
