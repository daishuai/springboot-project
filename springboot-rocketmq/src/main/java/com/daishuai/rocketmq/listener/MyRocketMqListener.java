package com.daishuai.rocketmq.listener;

import com.alibaba.fastjson.JSON;
import com.daishuai.rocketmq.entity.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @version 1.0.0
 * @description RocketMq消息消费者
 * @createTime 2023-03-25 17:32:26
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "group-1", topic = "HelloWorld")
public class MyRocketMqListener implements RocketMQListener<CommonMessage> {


    @Override
    public void onMessage(CommonMessage message) {
        log.info("接收到消息, 开始消费: {}", JSON.toJSONString(message));
    }
}
