package com.daishuai.websocket.server.apache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 19:23
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket")
public class MyWebSocketServer {
    
    private Session session;
    
    /**
     * 建立连接后触发的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("onOpen sessionId:{}", session.getId());
    }
    
    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        log.info("==========onClose sessionId:{} =======", session.getId());
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
        this.sendMessage(0, "WebSocket Server Message!", null);
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
    
    
    public void sendMessage(int status, String message, Object payload) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("payload", payload);
        try {
            this.session.getBasicRemote().sendText(result.toJSONString());
        } catch (IOException e) {
            log.error("发送消息失败：{}", e.getMessage());
        }
    }
}
