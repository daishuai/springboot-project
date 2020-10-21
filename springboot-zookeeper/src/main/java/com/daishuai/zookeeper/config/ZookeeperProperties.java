package com.daishuai.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Daishuai
 * @date 2020/10/19 15:32
 */
@Data
@ConfigurationProperties(prefix = "spring.zookeeper")
public class ZookeeperProperties {

    private Integer baseSleepTimeMs = 1000;

    private Integer maxRetries = 3;

    private String connect = "localhost:2181";

    private Integer sessionTimeoutMs = 5000;

    private Integer connectionTimeoutMs = 3000;

    private String namespace = "";
}
