package com.daishuai.mq.rabbit.consumer;

import com.daishuai.mq.rabbit.constant.RabbitMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/19 13:39
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = RabbitMQConstant.ITEM_QUEUE)
    public void onMessage(String message) {
        log.info("receive message from {}, message is : {}", RabbitMQConstant.ITEM_QUEUE, message);
    }
}
