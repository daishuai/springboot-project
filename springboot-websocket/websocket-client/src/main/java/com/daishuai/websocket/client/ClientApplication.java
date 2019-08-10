package com.daishuai.websocket.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 10:27
 */
@EnableScheduling
@SpringBootApplication
public class ClientApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
