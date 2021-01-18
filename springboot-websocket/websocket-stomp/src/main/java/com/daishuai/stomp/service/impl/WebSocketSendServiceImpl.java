package com.daishuai.stomp.service.impl;

import com.daishuai.stomp.service.WebSocketSendService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Daishuai
 * @date 2021/1/15 16:07
 */
@Service
public class WebSocketSendServiceImpl implements WebSocketSendService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendToUser(Object response, String destination, String... clientIds) {
        if (ArrayUtils.isEmpty(clientIds)) {
            return;
        }
        // 点对点
        for (String clientId : clientIds) {
            simpMessagingTemplate.convertAndSendToUser(clientId, "/topic/" + destination + "/pong", response);
        }

    }

    @Override
    public void sendToAll(Object response, String destination) {
        // 广播
        simpMessagingTemplate.convertAndSend("/user/topic" + destination + "/pong", response);
    }
}
