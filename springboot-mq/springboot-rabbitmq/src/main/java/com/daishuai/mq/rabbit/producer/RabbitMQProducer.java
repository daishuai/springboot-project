package com.daishuai.mq.rabbit.producer;

import com.daishuai.mq.rabbit.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/19 13:35
 */
@Component
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String routingKey, String message) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.ITEM_TOPIC_EXCHANGE, routingKey, message);
    }
}
