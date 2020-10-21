package com.daishuai.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @date 2020/10/19 15:30
 */
@Configuration
@EnableConfigurationProperties(value = ZookeeperProperties.class)
public class ZookeeperConfig {

    @Autowired
    private ZookeeperProperties properties;

    @Bean
    public CuratorFramework zookeeperClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries());
        CuratorFramework zookeeperClient = CuratorFrameworkFactory.builder()
                .connectString(properties.getConnect())
                .sessionTimeoutMs(properties.getSessionTimeoutMs())
                .connectionTimeoutMs(properties.getConnectionTimeoutMs())
                .namespace(properties.getNamespace())
                .retryPolicy(retryPolicy)
                .build();
        zookeeperClient.start();
        return zookeeperClient;
    }
}
