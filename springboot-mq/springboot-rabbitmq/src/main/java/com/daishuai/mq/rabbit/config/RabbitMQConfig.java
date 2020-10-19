package com.daishuai.mq.rabbit.config;

import com.daishuai.mq.rabbit.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @date 2020/10/19 13:23
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 声明交换机
     *
     * @return
     */
    @Bean
    public Exchange itemTopicExchange() {
        //durable 持久化
        return ExchangeBuilder.topicExchange(RabbitMQConstant.ITEM_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue itemQueue() {
        return QueueBuilder.durable(RabbitMQConstant.ITEM_QUEUE).build();
    }

    /**
     * 绑定队列和交换机
     *
     * @param itemQueue
     * @param itemTopicExchange
     * @return
     */
    @Bean
    public Binding itemBinding(Queue itemQueue, Exchange itemTopicExchange) {
        return BindingBuilder.bind(itemQueue).to(itemTopicExchange).with("item.#").noargs();
    }
}
