/*
package com.daishuai.websocket.server.config;

import com.daishuai.websocket.server.service.KdWebSocketService;
import com.daishuai.websocket.server.service.impl.KdWebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

*/
/**
 * @author Daishuai
 * @description WebSocket Config
 * @date 2019/8/10 11:03
 *//*

@Configuration
@ComponentScan("com.daishuai.websocket.server")
@EnableConfigurationProperties(value = {WebSocketProperties.class})
@EnableWebSocketMessageBroker
public class KdWebSocketConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketProperties webSocketProperties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        // 注册一个Stomp的节点（endpoint）,并指定使用SockJS协议。
        stompEndpointRegistry
                .addEndpoint(webSocketProperties.getEndPoint())
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 定义心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        taskScheduler.setDaemon(true);
        taskScheduler.initialize();

        // 服务端发送消息给客户端的域,多个用逗号隔开
        registry.enableSimpleBroker(webSocketProperties.getEnableSimpleBroker())
                // 定义心跳间隔 单位(ms)
                .setHeartbeatValue(new long[]{webSocketProperties.getHeartBeatInterval(), webSocketProperties.getHeartBeatInterval()})
                .setTaskScheduler(taskScheduler);
        // 定义webSocket前缀
        registry.setApplicationDestinationPrefixes(webSocketProperties.getApplicationDestinationPrefixes());
    }


    @Bean
    @ConditionalOnMissingBean
    public KdWebSocketService kdWebSocketService() {
        return new KdWebSocketServiceImpl();
    }
}
*/
