package com.daishuai.alert.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daishuai
 * @description
 * @date 2019/9/5 14:33
 */
@Configuration
public class KemAlertConfig {
    
    
    @Bean(initMethod = "init", destroyMethod = "destory")
    public Global global() {
        
        return new Global();
    }
}
