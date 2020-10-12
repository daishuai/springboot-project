package com.daishuai.mq.rocket.consumer;

import com.alibaba.fastjson.JSON;
import com.daishuai.mq.rocket.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/12 19:46
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQConstant.DEMO_TOPIC, consumerGroup = RocketMQConstant.CONSUMER_GROUP_DEMO_A)
public class DemoAConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String messageStr = new String(body);
        log.info("Demo Consumer A receive message: {}", messageStr);
    }
}
