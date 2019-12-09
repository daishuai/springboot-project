package com.daishuai.apache.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/16 11:26
 */
@RestController
public class DemoController {
    
    @GetMapping("/demo")
    public Object demo() {
        return new HashMap<>();
    }
}
