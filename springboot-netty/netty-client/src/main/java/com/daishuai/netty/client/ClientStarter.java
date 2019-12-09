package com.daishuai.netty.client;

import com.daishuai.netty.client.trigger.HeartBeatClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/10 13:47
 */
@Component
public class ClientStarter implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        HeartBeatClient client = new HeartBeatClient();
        client.run();
        client.sendData();
    }
}
