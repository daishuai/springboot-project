package com.daishuai.mq.active.producer;

import com.daishuai.mq.active.constant.ActiveMQConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author Daishuai
 * @date 2020/10/19 13:54
 */
@Component
public class ActiveMQProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue activeQueue;

    @Autowired
    private Topic activeTopic;

    public void sendMessage(String message) {
        jmsMessagingTemplate.convertAndSend(activeQueue, message);
        jmsMessagingTemplate.convertAndSend(activeTopic, message);
    }
}
