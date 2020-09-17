package com.daishuai.demo;

import com.daishuai.demo.config.DemoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/9/8 14:06
 */
@RestController
@EnableConfigurationProperties(value = DemoProperties.class)
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private DemoProperties demoProperties;

    @GetMapping(value = "/demo")
    public Object demo() {

        return demoProperties;
    }
}
