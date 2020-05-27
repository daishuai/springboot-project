package com.daishuai.hadoop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Daishuai
 * @date 2020/5/10 22:30
 */
@Data
@ConfigurationProperties(prefix = "hadoop.hdfs")
public class HadoopProperties {

    private String username;

    private String path;
}
