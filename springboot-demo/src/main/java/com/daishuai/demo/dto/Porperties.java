package com.daishuai.demo.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @date 2020/2/13 19:55
 */
@Data
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "kirms.security")
public class Porperties {

    private String[] pathPatterns;
}
