package com.daishuai.stomp.controller;

import com.daishuai.stomp.dto.RequestMessage;
import com.daishuai.stomp.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

/**
 * @author Daishuai
 * @date 2020/7/29 10:11
 */
@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    private SimpUserRegistry simpUserRegistry;


    @MessageMapping(value = "/welcome/ping")
    public ResponseMessage say(@RequestBody RequestMessage requestMessage, Principal principal) {

        Set<SimpUser> users = simpUserRegistry.getUsers();
        log.info("User Count:{}", simpUserRegistry.getUserCount());
        for (SimpUser user : users) {
            System.out.println("Username : " +  user.getName());
        }
        log.info("RequestMessage: {}", requestMessage);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Send To All");
        simpMessagingTemplate.convertAndSend("/user/topic/demo", responseMessage);
        responseMessage.setMessage("Send To User " + principal.getName());
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/topic/demo", responseMessage);
        return new ResponseMessage();
    }
}
