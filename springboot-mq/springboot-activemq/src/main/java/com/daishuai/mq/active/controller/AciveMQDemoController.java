package com.daishuai.mq.active.controller;

import com.daishuai.mq.active.producer.ActiveMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/10/19 13:55
 */
@RestController
public class AciveMQDemoController {

    @Autowired
    private ActiveMQProducer activeMQProducer;

    @GetMapping(value = "/demo")
    public Object sendMessage() {
        activeMQProducer.sendMessage("spring-boot-active-message");

        return 200;
    }
}
