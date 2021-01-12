package com.daishuai.redis.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/12/24 18:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class MultiRedisProperties {

    private Map<String, RedisProperties> servers;
}
