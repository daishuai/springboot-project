package com.daishuai.websocket.server.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description WebSocket Server Cache
 * @date 2019/8/10 12:13
 */
public class CommonCache {
    
    public static Map<String, Object> connectMachine = new HashMap<>();

    public static Map<String, String> sessionMap = new HashMap<>();
}
