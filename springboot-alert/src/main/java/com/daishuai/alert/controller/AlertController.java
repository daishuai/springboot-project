package com.daishuai.alert.controller;

import com.daishuai.alert.service.IAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/18 15:23
 */
@RestController
public class AlertController {
    
    @Autowired
    private IAlertService alertService;
    
    
    @GetMapping("/sendEvacuation/{onlineserial}")
    public Object sendEvacuationData(@PathVariable("onlineserial") String onlineserial) {
        Map<String, Object> result = new HashMap<>();
        int i = alertService.sendAGPEvacuationData(onlineserial);
        result.put("status", i);
        return result;
    }
}
