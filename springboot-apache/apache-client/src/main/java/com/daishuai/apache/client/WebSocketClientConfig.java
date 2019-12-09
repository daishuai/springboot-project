package com.daishuai.apache.client;

import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 20:28
 */
@Configuration
public class WebSocketClientConfig {
    
    //private String serverUri = "ws://localhost/kircp-servers/heartbeat/websocket";
    
    @Value("${kircp.serverUri:ws://localhost/kircp-servers/heartbeat/websocket}")
    private String serverUri;
    
    @Bean(initMethod = "connect")
    public MyWebSocketClient myWebSocketClient() throws URISyntaxException {
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("machineCode", "kem-frontline");
        httpHeaders.put("username", "无锡支队三屏机1");
        httpHeaders.put("password", "helloworld");
        return new MyWebSocketClient(new URI(serverUri), new Draft_6455(), httpHeaders);
    }
}
