package com.daishuai.netty.server;

import com.daishuai.netty.server.handler.HeartBeatServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 13:39
 */
@Component
public class ServerStarter implements ApplicationRunner {
    
    @Value("${server.port}")
    private int port;
    
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        HeartBeatServer heartBeatServer = new HeartBeatServer(9917);
        heartBeatServer.startServer();
    }
}
