package com.daishuai.websocket.server.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description DemoController
 * @date 2019/8/10 11:12
 */
@Slf4j
@RestController
public class DemoController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    
    
    @MessageMapping("/demo/ping")
    public void personalData(Map<String, Object> defaultVo) {
        log.info("接收到客户端发送的消息: {}", JSON.toJSONString(defaultVo));
    }

    @Scheduled(cron = "0/3 * * * * ?")
    public void socketTask() {
        Map<String, Object> message = new HashMap<>();
        message.put("code", "200");
        message.put("message", "处理成功");
        message.put("timestamp", System.currentTimeMillis());
        simpMessagingTemplate.convertAndSend("/user/demo/pong", message);
    }
    
}
