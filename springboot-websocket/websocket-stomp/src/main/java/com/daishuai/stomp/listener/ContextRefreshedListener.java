package com.daishuai.stomp.listener;

import com.alibaba.fastjson.JSON;
import com.daishuai.stomp.annotation.WebSocketMethod;
import com.daishuai.stomp.annotation.WebSocketService;
import com.daishuai.stomp.common.CommonCache;
import com.daishuai.stomp.config.InstantTask;
import com.daishuai.stomp.dto.InstantRequest;
import com.daishuai.stomp.service.InstantSocketTaskService;
import com.daishuai.stomp.service.WebSocketSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daishuai
 * @date 2021/1/15 9:38
 */
@Slf4j
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, Set<String>> taskMap = new ConcurrentHashMap<>();

    private final Map<String, Long> taskInterval = new ConcurrentHashMap<>();

    @Autowired
    private InstantSocketTaskService instantService;

    @Autowired
    private WebSocketSendService webSocketSendService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beanMap = event.getApplicationContext().getBeansWithAnnotation(WebSocketService.class);
            if (MapUtils.isEmpty(beanMap)) {
                return;
            }
            for (Object value : beanMap.values()) {
                Class<?> aClass = value.getClass();
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    WebSocketMethod annotation = method.getAnnotation(WebSocketMethod.class);
                    if (annotation == null) {
                        continue;
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    String businessType = annotation.businessType();
                    CommonCache.REQUEST_CLASS_MAP.put(businessType, (Class<? extends InstantRequest>) parameterTypes[0]);
                    CommonCache.BEAN_MAP.put(businessType, value);
                    String taskName = annotation.taskName();
                    long intervalSeconds = annotation.intervalSeconds();
                    CommonCache.METHOD_MAP.put(businessType, method);
                    taskInterval.compute(taskName, (key, val) -> val == null || val > intervalSeconds ? intervalSeconds : val);
                    taskMap.compute(taskName, (key, val) -> {
                        if (val == null) {
                            val = new HashSet<>();
                        }
                        val.add(businessType);
                        return val;
                    });
                }
            }

            for (Map.Entry<String, Set<String>> entry : taskMap.entrySet()) {
                log.info("开启定时任务, 任务名称: {}, 业务类型: {}", entry.getKey(), JSON.toJSONString(entry.getValue()));
                instantService.startInstantTask(entry.getKey(), new InstantTask(webSocketSendService, entry.getValue().toArray(new String[0])), 30, taskInterval.get(entry.getKey()));
            }
        }
    }
}
