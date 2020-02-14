package com.daishuai.demo.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/2/12 20:08
 */
@RestController
public class SecretConfig {

    @Value("#{${kirms.security.secretMap}}")
    private Map<String, String> secretMap = new HashMap<>();

    @Autowired
    private Porperties porperties;

    @GetMapping("/getSecretMap")
    public Object getSecretMap() {
        return porperties;
    }
}
