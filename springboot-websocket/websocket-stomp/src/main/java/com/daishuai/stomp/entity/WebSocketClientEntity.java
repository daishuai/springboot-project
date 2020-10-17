package com.daishuai.stomp.entity;

import java.security.Principal;

/**
 * @author Daishuai
 * @date 2020/10/16 22:58
 */
public class WebSocketClientEntity implements Principal {

    private final String clientId;

    public WebSocketClientEntity(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getName() {
        return clientId;
    }
}
