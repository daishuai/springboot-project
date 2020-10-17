package com.daishuai.stomp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.util.PathMatcher;

/**
 * @author Daishuai
 * @date 2020/10/16 22:14
 */
@Data
@ConfigurationProperties(prefix = "common.websocket")
public class WebSocketProperties {

    /**
     * 客户端通过该节点和服务端建立连接
     */
    private String[] endpoint = {"/ws/stomp"};

    /**
     * 跨域支持
     */
    private String allowedOrigins = "*";

    /**
     * 允许客户端订阅的主题
     */
    private String[] enableSimpleBroker = {"/queue", "/topic", "/user", "/client"};

    /**
     * 客户端向服务端发送消息是的前缀
     */
    private String[] applicationDestinationPrefixes = {"/ws"};

    /**
     *
     */
    private String userDestinationPrefix = "/user";

    /**
     *
     */
    private Integer cacheLimit = 1024;

    private Integer userRegistryOrder = 1;

    private Boolean preservePublishOrder = true;

    /**
     * 默认的心跳发送间隔
     */
    private Long heartbeatInterval = 10000L;
}
