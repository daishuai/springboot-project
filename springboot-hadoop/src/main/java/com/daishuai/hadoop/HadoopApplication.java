package com.daishuai.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Daishuai
 * @date 2020/2/11 12:30
 */
@Slf4j
@SpringBootApplication
public class HadoopApplication {

    public static void main(String[] args) {
        SpringApplication.run(HadoopApplication.class, args);
    }

}
