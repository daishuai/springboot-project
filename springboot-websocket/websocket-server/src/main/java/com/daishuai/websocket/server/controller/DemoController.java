package com.daishuai.websocket.server.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.websocket.server.service.KdWebSocketService;
import com.daishuai.websocket.server.vo.KdWebSocketMsgDefaultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
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

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/personalData/ping")
    @MessageMapping("/personalData/ping")
    public void personalData(@RequestBody KdWebSocketMsgDefaultVo defaultVo, Principal principal) {
        System.out.println(principal.getName());
        int userCount = simpUserRegistry.getUserCount();
        log.info("当前连接数: {}", userCount);
        for (SimpUser user : simpUserRegistry.getUsers()) {
            System.out.println(user.getName());
        }
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
        //kdWebSocketService.send(defaultVo);
        //defaultVo.setDestination("/user/personalData/pong");
        //kdWebSocketService.send(defaultVo);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/personalData/pong", "单点");
        simpMessagingTemplate.convertAndSend("/user/queue/personalData/pong", "广播");
    }


    @GetMapping("/demo")
    public Object get() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "WebSocket Server");
        return map;
    }

    @MessageMapping("/heartBeat/ping")
    public void heartBeat(KdWebSocketMsgDefaultVo vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Hello Websockt Client!");
        KdWebSocketMsgDefaultVo defaultVo = KdWebSocketMsgDefaultVo.builder()
                .payload(JSON.toJSONString(map))
                .userId(vo.getUserId())
                .destination("/heartBeat/pong").build();
        kdWebSocketService.send(vo.getUserId(), defaultVo);
    }

    /**
     * 获取真是ip
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("获取ip出错：{}", e.getMessage());
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }
}
