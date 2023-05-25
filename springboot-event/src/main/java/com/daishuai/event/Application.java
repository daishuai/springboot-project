package com.daishuai.event;

import com.daishuai.event.entity.CustomEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description Spring事件
 * @createTime 2023年05月25日 15:53:00
 */
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0/30 * * * * ?")
    public void publish() {
        CustomEvent event = new CustomEvent();
        event.setType("10");
        event.setPayload("HelloWorld");
        applicationEventPublisher.publishEvent(event);
    }

}
