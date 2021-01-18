package com.daishuai.stomp.controller;

import com.daishuai.stomp.dto.DemoRequest;
import com.daishuai.stomp.dto.ResponseMessage;
import com.daishuai.stomp.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Daishuai
 * @date 2020/7/29 10:11
 */
@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private DemoService demoService;


    @MessageMapping(value = "/welcome/ping")
    public ResponseMessage say(@RequestBody DemoRequest demoRequest, Principal principal) {
        log.info("DemoRequest: {}", demoRequest);
        demoService.demo(demoRequest, principal.getName());
        return new ResponseMessage();
    }
}
