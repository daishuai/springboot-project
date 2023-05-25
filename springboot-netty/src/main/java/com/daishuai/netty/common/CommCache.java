package com.daishuai.netty.common;

import com.daishuai.netty.model.NettySession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 通信缓存
 * @createTime 2022年08月18日 09:06:00
 */
public class CommCache {

    /**
     * key为clientId
     */
    public static final Map<String, NettySession> CLIENT_MAP = new ConcurrentHashMap<>();

    /**
     * key为channelId
     */
    public static final Map<String, NettySession> CHANNEL_MAP = new ConcurrentHashMap<>();
}
