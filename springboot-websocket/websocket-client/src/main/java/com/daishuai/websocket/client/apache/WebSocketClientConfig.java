package com.daishuai.websocket.client.apache;

import org.java_websocket.drafts.Draft_6455;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 20:28
 */
//@Configuration
public class WebSocketClientConfig {
    
    private String serverUri = "ws://localhost:8093/kircp/websocket";
    
    @Bean(initMethod = "connect")
    public MyWebSocketClient myWebSocketClient() throws URISyntaxException {
        return new MyWebSocketClient(new URI(serverUri), new Draft_6455());
    }
}
