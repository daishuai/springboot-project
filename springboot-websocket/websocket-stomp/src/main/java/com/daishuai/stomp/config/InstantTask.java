package com.daishuai.stomp.config;

import com.daishuai.stomp.common.CommonCache;
import com.daishuai.stomp.dto.InstantRequest;
import com.daishuai.stomp.service.WebSocketSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author Daishuai
 * @date 2021/1/14 19:17
 */
@Slf4j
public class InstantTask implements Runnable{

    private final WebSocketSendService webSocketSendService;

    private final String[] businessTypes;

    public InstantTask(WebSocketSendService webSocketSendService, String ... businessTypes) {
        this.webSocketSendService = webSocketSendService;
        this.businessTypes = businessTypes;
    }

    @Override
    public void run() {
        log.info("执行定时任务>>>>>>>>>>>>>>>>>>>>>");
        if (ArrayUtils.isEmpty(businessTypes)) {
            return;
        }
        for (String businessType : businessTypes) {
            Method method = CommonCache.METHOD_MAP.get(businessType);
            Object bean = CommonCache.BEAN_MAP.get(businessType);
            try {
                // 方法参数
                ConcurrentMap<String, InstantRequest> requestMap = CommonCache.REQUEST_MAP.get(businessType);
                if (MapUtils.isEmpty(requestMap)) {
                    continue;
                }
                Map<String, InstantRequest> newRequestMap = requestMap.values().stream().collect(Collectors.groupingBy(InstantRequest::getParamKey, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));
                for (InstantRequest instantRequest : newRequestMap.values()) {
                    Object response = method.invoke(bean, instantRequest, null);
                    webSocketSendService.sendToAll(response, "/" + instantRequest.getParamKey() + "/" + instantRequest.getBusinessType());
                }
            } catch (Exception e) {
                log.error("调用方法({})出错: {}", method.getName(), e.getMessage(), e);
            }
        }
    }
}
