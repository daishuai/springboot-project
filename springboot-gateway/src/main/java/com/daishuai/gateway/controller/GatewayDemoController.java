package com.daishuai.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 测试接口
 * @createTime 2024年01月09日 09:49:00
 */
@RestController
public class GatewayDemoController {

    @GetMapping(value = "/demo")
    public ResponseEntity<String> demo() {

        return ResponseEntity.ok("200");
    }
}
