package com.daishuai.apache.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 19:23
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket")
public class MyWebSocketServer {
    
    private MyService myService;
    
    public MyWebSocketServer(){

    }
    
    private final ConcurrentMap<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();
    private Session session;
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder().namingPattern("websocket-heartbeat-schedule-pool-%d").daemon(false).build());
    
    /**
     * 建立连接后触发的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        Map<String, Object> userProperties = session.getUserProperties();
        log.info("userPropertiest:{}", JSON.toJSONString(userProperties));
        System.out.println(session);
        this.session = session;
        log.info("onOpen sessionId:{}", session.getId());
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> this.sendMessage(session,0, "WebSocket Server Message!", null),
                1, 20, TimeUnit.SECONDS);
        scheduledFutureMap.put(session.getId(), scheduledFuture);
        //System.out.println(myService.getName());
    }
    
    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        System.out.println(session);
        log.info("==========onClose sessionId:{} =======", session.getId());
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(session.getId());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
    
    /**
     * 接收到客户端消息时触发的方法
     *
     * @param params
     * @param session
     */
    @OnMessage
    public void onMessage(String params, Session session) {
        log.info("收到来自sessionId:{} 的消息:{}", session.getId(), params);
    }
    
    /**
     * 发生错误时触发的方法
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("sessionId:{} 连接发送错误：{}", session.getId(), error.getMessage(), error);
    }
    
    
    public void sendMessage(Session session, int status, String message, Object payload) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("payload", payload);
        try {
            session.getBasicRemote().sendText(result.toJSONString());
        } catch (IOException e) {
            log.error("发送消息失败：{}", e.getMessage());
        }
    }
}
