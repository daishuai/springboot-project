package com.daishuai.alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/18 11:12
 */
@EnableScheduling
@SpringBootApplication
public class AlertApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AlertApplication.class, args);
    }
}
