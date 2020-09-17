package com.daishuai.stomp.config;

import com.daishuai.stomp.handler.SocketHandler;
import com.daishuai.stomp.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Daishuai
 * @date 2020/7/29 9:27
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), "/websocket-realtime")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*");
    }
}
