package com.daishuai.stomp.common;

import com.daishuai.stomp.dto.InstantRequest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Daishuai
 * @date 2021/1/14 19:13
 */
public class CommonCache {

    /**
     * 业务bean, key: businessType, value: Bean
     */
    public static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    /**
     * 业务方法名称, key: businessType, value: Method
     */
    public static final Map<String, Method> METHOD_MAP = new ConcurrentHashMap<>();

    /**
     * 业务方法参数类, key: businessType, value: InstantRequest.class
     */
    public static final Map<String, Class<? extends InstantRequest>> REQUEST_CLASS_MAP = new ConcurrentHashMap<>();

    /**
     * 业务参数, key1: businessType, key2: subscribeId, key3: InstantRequest
     */
    public static final Map<String, ConcurrentMap<String, InstantRequest>> REQUEST_MAP = new ConcurrentHashMap<>();

    /**
     * 订阅的topic, key: subscribeId, value: destination
     */
    public static final Map<String, String> SUBSCRIBE_TOPIC_MAP = new ConcurrentHashMap<>();

    /**
     * 增量推送的时间戳
     */
    public static final Map<String, Long> TIMESTAMP_MAP = new ConcurrentHashMap<>();

}
