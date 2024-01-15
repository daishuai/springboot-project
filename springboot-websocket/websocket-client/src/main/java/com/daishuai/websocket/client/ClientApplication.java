package com.daishuai.websocket.client;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Daishuai
 * @description WebSocket Client启动类
 * @date 2019/8/10 10:27
 */
@EnableScheduling
@SpringBootApplication
public class ClientApplication {
    
    public static void main(String[] args) {
        String password = "xiaofang123";
        String key = "sm4demo123456789";
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());
        String encryptHex = sm4.encryptHex(password);
        System.out.println(encryptHex);
        System.out.println(sm4.decryptStr(encryptHex));
        //SpringApplication.run(ClientApplication.class, args);
    }
}
