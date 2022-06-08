package com.daishuai.websocket.server.entity;

import lombok.Data;

import java.security.Principal;

/**
 * @author Daishuai
 * @date 2020/10/16 22:58
 */
@Data
public class WebSocketClientEntity implements Principal {

    private final String clientId;

    private String uniqueKey;

    private String userId;
    
    private String userName;

    public WebSocketClientEntity(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getName() {
        return clientId;
    }
}
