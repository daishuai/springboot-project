package com.daishuai.websocket.client.apache;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 19:36
 */
@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    
    public MyWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("---------MyWebSocketClient onOpen----------");
        this.send("WebSocket Client Message!");
    }
    
    @Override
    public void onMessage(String message) {
        log.info("接收到来自服务端的消息：{}", message);
    }
    
    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("---------MyWebSocketClient onClose----------");
        this.toReconnect();
        log.info("onClose -----");
    }
    
    @Override
    public void onError(Exception e) {
        log.info("---------MyWebSocketClient onError----------");
        //this.toReconnect();
    }
    
    private void toReconnect() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reconnect();
            }
        }, 5000L);
    }
}
