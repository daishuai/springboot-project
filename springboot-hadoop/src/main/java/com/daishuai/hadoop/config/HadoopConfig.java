package com.daishuai.hadoop.config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author Daishuai
 * @date 2020/5/10 22:33
 */
@Configuration
@EnableConfigurationProperties(HadoopProperties.class)
public class HadoopConfig {

    @Autowired
    private HadoopProperties properties;

    @Bean
    public FileSystem fileSystem() throws Exception {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        return FileSystem.get(new URI(properties.getPath()), configuration, properties.getUsername());
    }
}
