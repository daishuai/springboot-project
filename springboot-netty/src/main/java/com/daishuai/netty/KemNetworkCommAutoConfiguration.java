package com.daishuai.netty;

import com.daishuai.netty.config.NetworkCommProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 网络通信配置类
 * @createTime 2022年08月17日 10:12:00
 */
@ComponentScan
@Configuration
@EnableConfigurationProperties(value = {NetworkCommProperties.class})
public class KemNetworkCommAutoConfiguration {
}
