package com.daishuai.mq.rocket.consumer;

import com.alibaba.fastjson.JSON;
import com.daishuai.mq.rocket.constant.RocketMQConstant;
import com.daishuai.mq.rocket.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/12 18:59
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQConstant.DEMO_TOPIC, consumerGroup = RocketMQConstant.CONSUMER_GROUP_DEMO)
public class DemoConsumer implements RocketMQListener<MessageDto> {


    @Override
    public void onMessage(MessageDto message) {
        log.info("Demo Consumer receive message: {}", JSON.toJSONString(message));
    }
}
