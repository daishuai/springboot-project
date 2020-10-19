package com.daishuai.mq.active;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author Daishuai
 * @date 2020/10/19 13:53
 */
@EnableJms
@SpringBootApplication
public class ActiveMQApplcation {

    public static void main(String[] args) {
        SpringApplication.run(ActiveMQApplcation.class, args);
    }
}
