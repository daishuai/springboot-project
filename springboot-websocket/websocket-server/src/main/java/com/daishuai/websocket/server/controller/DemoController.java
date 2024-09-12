package com.daishuai.websocket.server.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.websocket.server.utils.AesEncryptUtils;
import com.daishuai.websocket.server.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/publicKey")
    public Map<String, Object> getPublicKey() {
        String publicKey = RSAUtil.generateBase64PublicKey();
        String key = AesEncryptUtils.initHexKey();
        Map<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        map.put("aesKey", key);
        return map;
    }


    @MessageMapping("/demo/ping")
    public void personalData(Map<String, Object> defaultVo) {
        log.info("接收到客户端发送的消息: {}", JSON.toJSONString(defaultVo));
    }

    @MessageMapping("/welcome/ping")
    public void welcomePing(Map<String, Object> defaultVo) throws Exception {
        log.info("接收到客户端发送的消息: {}", JSON.toJSONString(defaultVo));
        String password = MapUtils.getString(defaultVo, "password");
        String decryptPassword = RSAUtil.decryptBase64(password);
        log.info(decryptPassword);
    }

    @MessageMapping("/aesEncrypt/ping")
    public void aesEncrypt(Map<String, Object> defaultVo) throws Exception {
        log.info("接收到客户端发送的消息: {}", JSON.toJSONString(defaultVo));
        String password = MapUtils.getString(defaultVo, "password");
        String key = MapUtils.getString(defaultVo, "key");
        String decryptPassword = AesEncryptUtils.decrypt(password, key);
        log.info(decryptPassword);
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void socketTask() {
        log.info("发送消息");
        Map<String, Object> message = new HashMap<>();
        message.put("code", "200");
        message.put("message", "处理成功");
        message.put("timestamp", System.currentTimeMillis());
        message.put("destination", "/queue/demo/pong");
        message.put("type", "alert");
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "alert1");
        simpMessagingTemplate.convertAndSend("/user/abcd/demo/pong", message, headers);
        //simpMessagingTemplate.convertAndSendToUser("1234","/user/abcd/demo/pong", message);
    }

}
