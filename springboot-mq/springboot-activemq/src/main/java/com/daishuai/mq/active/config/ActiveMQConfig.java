package com.daishuai.mq.active.config;

import com.daishuai.mq.active.constant.ActiveMQConstant;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author Daishuai
 * @date 2020/10/19 14:30
 */
@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue activeQueue() {
        return new ActiveMQQueue(ActiveMQConstant.ACTIVE_QUEUE);
    }

    @Bean
    public Topic activeTopic() {
        return new ActiveMQTopic(ActiveMQConstant.ACTIVE_TOPIC);
    }

    @Bean(value = "queueListenerContainerFactory")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean(value = "topicListenerContainerFactory")
    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
}
