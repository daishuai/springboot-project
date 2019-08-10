package com.daishuai.websocket.server.service.impl;

import com.daishuai.websocket.server.service.KdWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 11:07
 */
public class KdWebSocketServiceImpl implements KdWebSocketService {
    
    @Autowired
    private SimpMessagingTemplate template;
    
    @Override
    public SimpMessagingTemplate getTemplate() {
        return template;
    }
}
