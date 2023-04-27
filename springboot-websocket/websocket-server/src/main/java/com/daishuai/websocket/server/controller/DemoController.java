package com.daishuai.websocket.server.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.websocket.server.service.KdWebSocketService;
import com.daishuai.websocket.server.vo.KdWebSocketMsgDefaultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @Autowired
    private KdWebSocketService kdWebSocketService;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/server/monitor/health/status")
    public String health() {
        return "ok";
    }
    
    
    @MessageMapping("/personalData/ping")
    public void personalData(KdWebSocketMsgDefaultVo defaultVo) {
        String payload = defaultVo.getPayload();
        String destination = defaultVo.getDestination();
        String clientId = defaultVo.getClientId();
        String userId = defaultVo.getUserId();
        log.info("userId:{}\nclientId:{}\ndestination:{}\npayload:{}\n", userId, destination, clientId, payload);
        Map<String, Object> message = new HashMap<>();
        message.put("code", "200");
        message.put("message", "处理成功");
        message.put("timestamp", System.currentTimeMillis());
        message.put("result", defaultVo);
        defaultVo.setPayload(JSON.toJSONString(message));
        kdWebSocketService.send(defaultVo);
        defaultVo.setDestination("/user/" + clientId + "/personalData/pong1");
        kdWebSocketService.send(defaultVo);
        defaultVo.setDestination("/user/" + clientId + "/personalData/pong2");
        kdWebSocketService.send(defaultVo);
    }

    @Scheduled(cron = "0/3 * * * * ?")
    public void socketTask() {
        Map<String, Object> message = new HashMap<>();
        message.put("code", "200");
        message.put("message", "处理成功");
        message.put("timestamp", System.currentTimeMillis());
        stringRedisTemplate.opsForValue().set("name", "helloworld");
        simpMessagingTemplate.convertAndSend("/user/demo/pong", message);
    }
    
}
