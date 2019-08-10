package com.daishuai.websocket.client.controller;

import com.daishuai.websocket.client.dto.KemWebSocketRequest;
import com.daishuai.websocket.client.handler.DemoSessionHandler;
import com.daishuai.websocket.client.service.KemWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description DemoController
 * @date 2019/8/10 13:42
 */
@RestController
public class DemoController {
    
    
    @Autowired(required = false)
    private KemWebSocketClient kemWebSocketClient;
    
    @GetMapping("/send")
    public Object sendMessage() {
        KemWebSocketRequest request = new KemWebSocketRequest();
        request.setSendDestination("/ws/personalData/ping");
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "default");
        param.put("clientId", "default");
        param.put("destination", "/user/default/personalData/pong");
        param.put("payload", "Hello Server, I'm Client1!");
        request.setParam(param);
        kemWebSocketClient.sendMessage(request);
        return new HashMap<>();
    }
}
