package com.daishuai.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/9/8 14:07
 */
@ConfigurationProperties(prefix = "commons.authz.security")
@Data
@Component
public class DemoProperties {

    private String[] permitAllUrl;
}
