package com.daishuai.mq.rocket.producer;

import com.alibaba.fastjson.JSON;
import com.daishuai.mq.rocket.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/12 18:59
 */
@Slf4j
@Component
public class DemoProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     *
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, MessageDto message) {

        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("send message success, sendResult : {}", JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("send message occur error : {} ", throwable.getMessage(), throwable);
            }
        });

    }
}
