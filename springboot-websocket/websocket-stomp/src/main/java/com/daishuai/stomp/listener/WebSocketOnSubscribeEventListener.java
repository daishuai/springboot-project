package com.daishuai.stomp.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daishuai.stomp.common.CommonCache;
import com.daishuai.stomp.dto.InstantRequest;
import com.daishuai.stomp.service.InstantSocketTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

/**
 * 监听订阅地址的用户
 *
 * @author daishuai
 */
@Slf4j
@Component
public class WebSocketOnSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Autowired
    private InstantSocketTaskService instantSocketTaskService;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        String clientId = sha.getUser().getName();
        log.info("On Subscribe,  username is {}", clientId);

        String params = sha.getFirstNativeHeader("params");
        if (StringUtils.isBlank(params)) {
            return;
        }
        String destination = sha.getDestination();
        String subscriptionId = sha.getSubscriptionId();
        CommonCache.SUBSCRIBE_TOPIC_MAP.put(subscriptionId, destination);
        String[] paths = destination.split("/");
        if (paths.length < 2) {
            log.warn("订阅地址有问题:{}", destination);
            return;
        }
        String businessType = paths[paths.length - 2];
        Class<? extends InstantRequest> aClass = CommonCache.REQUEST_CLASS_MAP.get(businessType);
        InstantRequest instantRequest = JSON.parseObject(params).toJavaObject(aClass);
        //立即执行一次
        instantSocketTaskService.startAtOnce(instantRequest, clientId);
        // 加入任务
        instantSocketTaskService.joinInScheduledTask(String.valueOf(clientId), subscriptionId, businessType, instantRequest);
    }
}
