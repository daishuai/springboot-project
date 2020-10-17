package com.daishuai.stomp.config;

import com.daishuai.stomp.interceptor.WebSocketHandshakeInterceptor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Daishuai
 * @date 2020/7/29 10:16
 */
@Configuration
@EnableConfigurationProperties(value = WebSocketProperties.class)
@EnableWebSocketMessageBroker
public class WebStompSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketProperties webSocketProperties;

    @Autowired
    private HandshakeInterceptor handshakeInterceptor;

    @Autowired(required = false)
    private ChannelInterceptor[] channelInterceptors;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketProperties.getEndpoint())
                .addInterceptors(handshakeInterceptor)
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

        registry.enableSimpleBroker(webSocketProperties.getEnableSimpleBroker())
                .setHeartbeatValue(new long[]{webSocketProperties.getHeartbeatInterval(), webSocketProperties.getHeartbeatInterval()})
                .setTaskScheduler(taskScheduler);

        registry.setApplicationDestinationPrefixes(webSocketProperties.getApplicationDestinationPrefixes())
                .setUserDestinationPrefix(webSocketProperties.getUserDestinationPrefix())
                .setCacheLimit(webSocketProperties.getCacheLimit())
                .setPreservePublishOrder(webSocketProperties.getPreservePublishOrder())
                .setUserRegistryOrder(webSocketProperties.getUserRegistryOrder());
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        if (ArrayUtils.isNotEmpty(channelInterceptors)) {
            registration.interceptors(channelInterceptors);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public HandshakeInterceptor handshakeInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }
}
