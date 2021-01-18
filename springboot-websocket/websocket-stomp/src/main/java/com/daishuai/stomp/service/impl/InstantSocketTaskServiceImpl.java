package com.daishuai.stomp.service.impl;

import com.daishuai.stomp.common.CommonCache;
import com.daishuai.stomp.config.InstantTask;
import com.daishuai.stomp.dto.InstantRequest;
import com.daishuai.stomp.service.InstantSocketTaskService;
import com.daishuai.stomp.service.WebSocketSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author Daishuai
 * @date 2021/1/14 19:15
 */
@Slf4j
@Service
public class InstantSocketTaskServiceImpl implements InstantSocketTaskService, InitializingBean {

    @Value(value = "${incidentManage.data.sync.scheduledExecutor.corePoolSize:8}")
    private Integer corePoolSize;

    @Autowired
    private WebSocketSendService webSocketSendService;

    private ScheduledExecutorService scheduledExecutorService;

    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    /**
     * key: clientId, value: subscribeIds
     */
    private final ConcurrentMap<String, Set<String>> subscribeMap = new ConcurrentHashMap<>();

    /**
     * key: clientId, value: businessTypes
     */
    private final ConcurrentMap<String, Set<String>> businessTypeMap = new ConcurrentHashMap<>();


    @Override
    public void startInstantTask(String taskName, InstantTask task, long initialDelay, long delay) {
        log.info("开启一个任务:【taskName:{}】>>>>>>>>>>>>>>>>>", taskName);
        scheduledFutureMap.computeIfAbsent(taskName, key -> scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.SECONDS));
    }

    @Override
    public void stopInstantTask(String... taskNames) {
        for (String taskName : taskNames) {
            scheduledFutureMap.computeIfPresent(taskName, (key, val) -> {
                val.cancel(false);
                return val;
            });
            scheduledFutureMap.remove(taskName);
        }
    }

    @Override
    public void joinInScheduledTask(String clientId, String subscribeId, String businessType, InstantRequest instantRequest) {
        Set<String> userSet = subscribeMap.computeIfAbsent(clientId, key -> new HashSet<>());
        userSet.add(subscribeId);
        ConcurrentMap<String, InstantRequest> map = CommonCache.REQUEST_MAP.computeIfAbsent(businessType, key -> new ConcurrentHashMap<>());
        map.put(subscribeId, instantRequest);
        Set<String> businessTypes = businessTypeMap.computeIfAbsent(clientId, key -> new HashSet<>());
        businessTypes.add(businessType);
    }

    @Override
    public void startAtOnce(InstantRequest socketRequest, String ... clientIds) {
        String businessType = socketRequest.getBusinessType();
        // 获取方法实例
        Method method = CommonCache.METHOD_MAP.get(businessType);
        Object bean = CommonCache.BEAN_MAP.get(businessType);
        try {
            Object response = method.invoke(bean, socketRequest, clientIds);
            webSocketSendService.sendToUser(response, "/" + socketRequest.getParamKey() + "/" + socketRequest.getBusinessType(), clientIds);
        } catch (Exception e) {
            log.error("方法({})出错: {}", method.getName(), e.getMessage(), e);
        }
    }

    @Override
    public void removeFromScheduledTask(String clientId, String subscribeId) {
        String topic = CommonCache.SUBSCRIBE_TOPIC_MAP.get(subscribeId);
        if (StringUtils.isBlank(topic)) {
            return;
        }
        String[] paths = topic.split("/");
        if (paths.length >= 2) {
            String businessType = paths[paths.length - 2];
            this.remove(clientId, subscribeId, businessType);
        }
    }

    @Override
    public void removeAllFromScheduledTask(String clientId) {
        Set<String> businessTypes = businessTypeMap.get(clientId);
        for (String value : businessTypes) {
            this.remove(clientId, null, value);
        }
        subscribeMap.remove(clientId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("incident-count-schedule-pool-%d").daemon(false).build());
    }

    /**
     * 取消订阅
     *
     * @param clientId 客户端ID
     * @param subscribeId 订阅ID
     * @param businessType 业务类型
     */
    private void remove(String clientId, String subscribeId, String businessType) {
        ConcurrentMap<String, InstantRequest> requestMap = CommonCache.REQUEST_MAP.get(businessType);
        if (MapUtils.isEmpty(requestMap)) {
            return;
        }
        // 移除指定取消订阅的任务
        if (StringUtils.isNotBlank(subscribeId)) {
            InstantRequest instantRequest = requestMap.get(subscribeId);
            requestMap.remove(subscribeId);
            CommonCache.TIMESTAMP_MAP.remove(businessType + clientId + instantRequest.getParamKey());
            CommonCache.SUBSCRIBE_TOPIC_MAP.remove(subscribeId);
            return;
        }
        // 如果subscribeId为空移除所有任务
        Set<String> subscribeIds = subscribeMap.get(clientId);
        if (CollectionUtils.isEmpty(subscribeIds)) {
            return;
        }
        for (String id : subscribeIds) {
            InstantRequest instantRequest = requestMap.get(id);
            requestMap.remove(id);
            CommonCache.TIMESTAMP_MAP.remove(businessType + clientId + instantRequest.getParamKey());
            CommonCache.SUBSCRIBE_TOPIC_MAP.remove(id);
        }
        subscribeMap.remove(clientId);
    }
}
