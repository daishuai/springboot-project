package com.daishuai.netty.config;

import com.daishuai.netty.bootstrap.NetWorkCommClient;
import com.daishuai.netty.bootstrap.NetworkCommServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 配置
 * @createTime 2022年08月17日 14:49:00
 */
@Configuration
public class NetworkCommConfig {

    @Resource
    private NetworkCommProperties properties;

    @Bean
    @ConditionalOnProperty(name = "kem.suite.network-comm.mode", havingValue = "server")
    public NetworkCommServer networkCommServer() {
        return new NetworkCommServer(properties.getServer());
    }

    @Bean
    @ConditionalOnProperty(name = "kem.suite.network-comm.mode", havingValue = "client")
    public NetWorkCommClient netWorkCommClient() {
        return new NetWorkCommClient(properties.getClient());
    }
}
