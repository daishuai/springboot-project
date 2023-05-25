package com.daishuai.event.listener;

import com.alibaba.fastjson.JSON;
import com.daishuai.event.entity.CustomEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 事件监听器
 * @createTime 2023年05月25日 15:59:00
 */
@Slf4j
@Component
public class CustomEventListener {

    @EventListener
    public void processCustomEvent(CustomEvent event) {
        log.info("EVENT: {}", JSON.toJSONString(event));
    }
}
