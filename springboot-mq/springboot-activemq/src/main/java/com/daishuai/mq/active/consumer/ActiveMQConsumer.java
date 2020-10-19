package com.daishuai.mq.active.consumer;

import com.daishuai.mq.active.constant.ActiveMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/19 13:55
 */
@Slf4j
@Component
public class ActiveMQConsumer {

    @JmsListener(destination = ActiveMQConstant.ACTIVE_QUEUE, containerFactory = "queueListenerContainerFactory")
    public void onQueueMessage(String message) {
        log.info("receive message from queue: {}, message body is: {}", ActiveMQConstant.ACTIVE_QUEUE, message);
    }

    @JmsListener(destination = ActiveMQConstant.ACTIVE_TOPIC, containerFactory = "topicListenerContainerFactory")
    public void onTopicMessage(String message) {
        log.info("receive message from topic: {}, message body is: {}", ActiveMQConstant.ACTIVE_TOPIC, message);
    }
}
