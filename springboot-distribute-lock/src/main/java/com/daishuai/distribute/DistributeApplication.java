package com.daishuai.distribute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Daishuai
 * @date 2020/9/21 10:30
 */
@MapperScan(value = "com.daishuai.distribute.dao")
@SpringBootApplication
public class DistributeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeApplication.class, args);
    }
}
