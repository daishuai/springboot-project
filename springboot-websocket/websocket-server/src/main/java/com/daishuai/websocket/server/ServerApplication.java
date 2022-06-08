package com.daishuai.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Daishuai
 * @description WebSocket Server启动类
 * @date 2019/8/10 10:28
 */
@EnableScheduling
@SpringBootApplication
public class ServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
