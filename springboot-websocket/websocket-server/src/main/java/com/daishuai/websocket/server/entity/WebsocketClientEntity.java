package com.daishuai.websocket.server.entity;

import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

/**
 * @author Daishuai
 * @date 2020/8/20 17:03
 */
@Slf4j
public final class WebsocketClientEntity implements Principal {

    private final String name;

    public WebsocketClientEntity(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        log.info("getName: {}", name);
        return name;
    }
}
