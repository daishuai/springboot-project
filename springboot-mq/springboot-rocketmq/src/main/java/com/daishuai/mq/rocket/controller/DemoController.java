package com.daishuai.mq.rocket.controller;

import com.daishuai.mq.rocket.constant.RocketMQConstant;
import com.daishuai.mq.rocket.dto.MessageDto;
import com.daishuai.mq.rocket.producer.DemoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/10/12 19:00
 */
@RestController
public class DemoController {

    @Autowired
    private DemoProducer demoProducer;


    @GetMapping(value = "/demo")
    public int demo() {
        MessageDto<String> messageDto = new MessageDto();
        messageDto.setType("demo");
        messageDto.setBody("HelloWorld");
        messageDto.setSource("RocketMQApplication");
        demoProducer.sendMessage(RocketMQConstant.DEMO_TOPIC, messageDto);
        return 200;
    }
}
