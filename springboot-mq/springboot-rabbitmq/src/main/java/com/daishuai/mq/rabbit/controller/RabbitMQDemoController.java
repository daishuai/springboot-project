package com.daishuai.mq.rabbit.controller;

import com.daishuai.mq.rabbit.producer.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/10/19 13:37
 */
@RestController
public class RabbitMQDemoController {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;


    @GetMapping(value = "/demo")
    public Object sendMessage() {
        rabbitMQProducer.sendMessage("item.springboot", "spring-boot-rabbitmq");
        return 200;
    }
}
