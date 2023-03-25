package com.daishuai.rocketmq;

import com.daishuai.rocketmq.entity.CommonMessage;
import com.daishuai.rocketmq.service.RocketMqHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @version 1.0.0
 * @description 启动类
 * @createTime 2023-03-25 17:11:34
 */
@RestController
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    private RocketMqHelper rocketMqHelper;

    @GetMapping(value = "/send")
    public String producer() {
        CommonMessage message = new CommonMessage();
        message.setId("1234567890");
        message.setType("HelloWorld");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "Tom");
        dataMap.put("age", 123);
        message.setPayload(dataMap);
        Message<CommonMessage> build = MessageBuilder.withPayload(message).build();
        rocketMqHelper.asyncSend("HelloWorld", build);
        return "ok";
    }
}
