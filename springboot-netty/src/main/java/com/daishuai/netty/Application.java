package com.daishuai.netty;

import com.daishuai.netty.model.TcpMessageModel;
import com.daishuai.netty.service.MessageSendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 启动类
 * @createTime 2023年05月25日 17:14:00
 */
@EnableScheduling
@SpringBootApplication
public class Application {

    @Value("${kem.suite.network-comm.mode}")
    private String source;

    @Resource
    private MessageSendService messageSendService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendMessage() {
        TcpMessageModel messageModel = new TcpMessageModel();
        messageModel.setAck(1);
        messageModel.setType("10");
        messageModel.setSource(source);
        messageSendService.sendMessage(messageModel);
    }
}
