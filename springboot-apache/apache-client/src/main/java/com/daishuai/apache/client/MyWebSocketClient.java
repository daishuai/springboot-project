package com.daishuai.apache.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 19:36
 */
@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("websocket-heartbeat-schedule-pool-%d").daemon(true).build());
    
    private Future future;
    
    
    public MyWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    
    public MyWebSocketClient(URI serverUri, Draft draft, Map<String, String> httpHeaders) {
        super(serverUri, draft, httpHeaders);
    }
    
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("---------MyWebSocketClient onOpen----------");
        /*future = scheduledExecutorService.scheduleAtFixedRate(this::sendPing,
                1, 10, TimeUnit.SECONDS);*/
    }
    
    @Override
    public void onMessage(String message) {
        log.info("接收到来自服务端的消息：{}", message);
    }
    
    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("---------MyWebSocketClient onClose----------");
        //future.cancel(true);
        scheduledExecutorService.schedule(this::reconnect, 5L, TimeUnit.SECONDS);
    }
    
    @Override
    public void onError(Exception e) {
        log.info("---------MyWebSocketClient onError----------");
    }
    
}
